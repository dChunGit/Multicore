#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cuda.h>

using namespace std;

#define THREADS 64

__global__ void buckets_global(int* data, int* result, int total) {
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    if (index < total) {
		if (data[index] >= 0 && data[index] <= 99) atomicAdd(result, 1);
		else if (data[index] >= 100 && data[index] <= 199) atomicAdd(result + 1, 1);
		else if (data[index] >= 200 && data[index] <= 299) atomicAdd(result + 2, 1);
		else if (data[index] >= 300 && data[index] <= 399) atomicAdd(result + 3, 1);
		else if (data[index] >= 400 && data[index] <= 499) atomicAdd(result + 4, 1);
		else if (data[index] >= 500 && data[index] <= 599) atomicAdd(result + 5, 1);
		else if (data[index] >= 600 && data[index] <= 699) atomicAdd(result + 6, 1);
		else if (data[index] >= 700 && data[index] <= 799) atomicAdd(result + 7, 1);
		else if (data[index] >= 800 && data[index] <= 899) atomicAdd(result + 8, 1);
		else if (data[index] >= 900 && data[index] <= 999) atomicAdd(result + 9, 1);
	}
}

__global__ void buckets_local(int*data, int* result, int total) {
    extern __shared__ int local[];
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    if (index < total) {
		if (data[index] >= 0 && data[index] <= 99) atomicAdd(local, 1);
		else if (data[index] >= 100 && data[index] <= 199) atomicAdd(local + 1, 1);
		else if (data[index] >= 200 && data[index] <= 299) atomicAdd(local + 2, 1);
		else if (data[index] >= 300 && data[index] <= 399) atomicAdd(local + 3, 1);
		else if (data[index] >= 400 && data[index] <= 499) atomicAdd(local + 4, 1);
		else if (data[index] >= 500 && data[index] <= 599) atomicAdd(local + 5, 1);
		else if (data[index] >= 600 && data[index] <= 699) atomicAdd(local + 6, 1);
		else if (data[index] >= 700 && data[index] <= 799) atomicAdd(local + 7, 1);
		else if (data[index] >= 800 && data[index] <= 899) atomicAdd(local + 8, 1);
		else if (data[index] >= 900 && data[index] <= 999) atomicAdd(local + 9, 1);
	}
    __syncthreads();

    int base_index = blockIdx.x * 10;
    for (int i = 0; i < 10; i++) {
    	result[base_index + i] = local[i];
    }
}

__global__ void reduce_buckets_local(int*data, int*result, int num_blocks) {
	int index = blockIdx.x;
	int count = 0;
	for (int i = 0; i < num_blocks; i++) {
		count += data[(10*i) + index];
	}
	result[index] = count;
}


int main(int argc,char **argv)
{

	// initialization
    vector<int> array;
    int i = 0;

    ifstream file( "inp.txt" );
    int number;
    while(file>>number) {
        array.push_back(number); 
        i++;
        if (file.peek() == ',')
            file.ignore();
    }

    // structures used by all
    int data_size = sizeof(int)*array.size();
    int result_size = sizeof(int)*10;
    int total = array.size();
    int* data = (int*)malloc(data_size);
    int num_blocks = array.size()/THREADS;
    if (array.size()%THREADS != 0) {
    	num_blocks++;
    }
    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
    }

    int* d_data;
	cudaMalloc((void **)&d_data, data_size);
    cudaMemcpy(d_data, data, data_size, cudaMemcpyHostToDevice);

    // 2A
    int* result1 = (int*)malloc(result_size);
    for (int i = 0; i < 10; i++) {
    	result1[i] = 0;
    }
    int* d_result1;
    cudaMalloc((void **)&d_result1, result_size);
    cudaMemcpy(d_result1, result1, result_size, cudaMemcpyHostToDevice);

    buckets_global<<<num_blocks, THREADS>>>(d_data, d_result1, total);

    cudaMemcpy(result1, d_result1, result_size, cudaMemcpyDeviceToHost);

    // 2B
    int* result_inter = (int*)malloc(result_size*num_blocks);
    for (int i = 0; i < 10*num_blocks; i++) {
    	result_inter[i] = 0;
    }
    int* d_result_inter;
    cudaMalloc((void **)&d_result_inter, result_size*num_blocks);
    cudaMemcpy(d_result_inter, result_inter, result_size*num_blocks, cudaMemcpyHostToDevice);
    
    int* result2 = (int*)malloc(result_size);
    for (int i = 0; i < 10; i++) {
    	result2[i] = 0;
    }
    int* d_result2;
    cudaMalloc((void **)&d_result2, result_size);
    cudaMemcpy(d_result2, result2, result_size, cudaMemcpyHostToDevice);

    buckets_local<<<num_blocks, THREADS, result_size>>>(d_data, d_result_inter, total);

    cudaMemcpy(result_inter, d_result_inter, result_size*num_blocks, cudaMemcpyDeviceToHost);

    reduce_buckets_local<<<10, 1>>>(d_result_inter, d_result2, num_blocks);

    cudaMemcpy(result2, d_result2, result_size, cudaMemcpyDeviceToHost);

    //2C


    FILE *fp = fopen("q2a.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < 9; a++) {
            fprintf(fp, "%d, ", result1[a]);
        }
        fprintf(fp, "%d", result1[9]);
        fclose(fp);
    }

    fp = fopen("q2b.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < 9; a++) {
            fprintf(fp, "%d, ", result2[a]);
        }
        fprintf(fp, "%d", result2[9]);
        fclose(fp);
    }

    // force the printf()s to flush
    cudaDeviceSynchronize();

    return 0;
}
