#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cuda.h>

using namespace std;

#define THREADS 64

__global__ void last_digits(int* mod, int* data, int n) {
    int thid = blockIdx.x * blockDim.x + threadIdx.x;
    if(thid < n) {
        mod[thid] = data[thid]%10;
    }
}

__global__ void min_reduction(int * data, int* results, int n) {
    extern __shared__ int temp[];
    int thid = blockIdx.x*blockDim.x + threadIdx.x;
    int lid = threadIdx.x;

    if(thid < n) {
        temp[lid] = data[thid];
    } else temp[lid] = 1000;
    __syncthreads();

    for(int offset = blockDim.x>>1; offset > 0; offset >>= 1) {
        __syncthreads();
        if(lid < offset) {
            if(temp[lid + offset] < temp[lid]) {
                temp[lid] = temp[lid + offset];
            }
        }
    }

    if(lid == 0) {
        results[blockIdx.x] = temp[0];
    }
}

int main(int argc,char **argv) {
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

    int* data = new int[array.size()];
    int* mod = new int[array.size()];
    int* d_data;
    int* d_mod;
    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
    }

    int size = sizeof(int)*array.size();

    cudaMalloc((void **) &d_data, size);
    cudaMalloc((void **) &d_mod, size);

    cudaMemcpy(d_data, data, size, cudaMemcpyHostToDevice);

    int sizing = array.size()/THREADS;
    if(array.size()%THREADS > 0) {
        sizing++;
    }

    int* inter = new int[array.size()];
    int* d_inter;
    int blockSize = sizeof(int)*THREADS;
    cudaMalloc((void **) &d_inter, size);

    // first reduction
    min_reduction<<<sizing, THREADS, blockSize>>>(d_data, d_inter, array.size());
    cudaMemcpy(inter, d_inter, size, cudaMemcpyDeviceToHost);

    // second reduction
    int* results = new int[array.size()];
    int* d_results;
    cudaMalloc((void **) &d_results, size);
    
    min_reduction<<<sizing, THREADS, blockSize>>>(d_inter, d_results, sizing);
    cudaMemcpy(results, d_results, blockSize, cudaMemcpyDeviceToHost);

    // last digits of array
    last_digits<<<sizing, THREADS>>>(d_mod, d_data, array.size());
    cudaMemcpy(mod, d_mod, size, cudaMemcpyDeviceToHost);

    FILE *fp = fopen("q1b.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < array.size(); a++) {
            fprintf(fp, "%d", mod[a]);
            if(a + 1 < array.size()) {
                fprintf(fp, ", ");
            }
        }
        fclose(fp);
    }

    fp = fopen("q1a.txt", "w");
    if(fp != NULL && array.size() > 0) {
        fprintf(fp, "%d", results[0]);
        fclose(fp);
    }

    cudaFree(d_data);
    cudaFree(d_inter);
    cudaFree(d_results);
    cudaFree(d_mod);

    // force the printf()s to flush
    cudaDeviceSynchronize();


    return 0;
}
