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
		if (data[index] >= 0 && data[index] <= 99) atomicAdd(data, 1);
		else if (data[index] >= 100 && data[index] <= 199) atomicAdd(data + 1, 1);
		else if (data[index] >= 200 && data[index] <= 299) atomicAdd(data + 2, 1);
		else if (data[index] >= 300 && data[index] <= 399) atomicAdd(data + 3, 1);
		else if (data[index] >= 400 && data[index] <= 499) atomicAdd(data + 4, 1);
		else if (data[index] >= 500 && data[index] <= 599) atomicAdd(data + 5, 1);
		else if (data[index] >= 600 && data[index] <= 699) atomicAdd(data + 6, 1);
		else if (data[index] >= 700 && data[index] <= 799) atomicAdd(data + 7, 1);
		else if (data[index] >= 800 && data[index] <= 899) atomicAdd(data + 8, 1);
		else if (data[index] >= 900 && data[index] <= 999) atomicAdd(data + 9, 1);
	}
}

__global__ void buckets_local(int*data, int* result, int num_index) {

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
    int size = sizeof(int)*array.size();
    int total = array.size();
    int* data = (int*)malloc(size);
    int num_blocks = array.size()/THREADS;
    if (array.size()%THREADS != 0) {
    	num_blocks++;
    }
    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
    }

    int* d_data;
	cudaMalloc((void **)&d_data, size);
    cudaMemCpy(d_data, data, size, cudaMemCpyHostToDevice);

    // 2A
    int* result1 = (int*)malloc(10*sizeof(int));
    for (int i = 0; i < 10; i++) {
    	result1[i] = 0;
    }
    int* d_result1;
    cudaMalloc((void **)&d_result1, 10);
    cudaMemCpy(d_result1, result1, 10, cudaMemCpyHostToDevice);

    buckets_global<<<num_blocks, THREADS>>>(d_data, d_result1, total);

    cudaMemCpy(results1, d_result1, size, cudaMemcpyDeviceToHost);

    // 2B
    int* result2 = (int*)malloc(10*sizeof(int)*num_blocks);
    for (int i = 0; i < 10; i++) {
    	result[i] = 0;
    }
    int* d_result2;
    cudaMalloc((void **)&d_result2, 10);
    cudaMemCpy(d_result2, result2, 10, cudaMemCpyHostToDevice);

    buckets_local<<<NUM_BLOCKS, 1>>>(d_data, d_result2, total);


    FILE *file = fopen("q2a.txt", "w");
    if(file != NULL) {
        for(int a = 0; a < 9; a++) {
            fprintf(fp, "%d, ", result1[a]);
        }
        fprintf(fp, "%d", result1[9]);
        fclose(file);
    }

    // force the printf()s to flush
    cudaDeviceSynchronize();

    return 0;
}
