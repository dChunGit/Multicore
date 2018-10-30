#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
// #include <cuda.h>

using namespace std;

#define NUM_BLOCKS 16
#define BLOCK_WIDTH 1

__global__ void setup(int* small) {
    small[blockIdx.x] = 0;
    if(blockIdx.x == 1) {
        small[blockIdx.x] = 1;
    }
    __syncthreads();
}

__global__ void min(int* small, int* data) {
    printf("Thread %d read %d with value %d \n", blockIdx.x, data[blockIdx.x], small[blockIdx.x]);
    __syncthreads();
}

__global__ void finish(int* small, int* data, int* max) {
    if(small[blockIdx.x] == 1) {
        *max = data[blockIdx.x];
    }
}

int main(int argc,char **argv) {
    vector<int> array;
    int i = 0;

    ifstream file( "inp2.txt" );
    int number;
    char delimiter;
    while((file >> number >> delimiter) && (delimiter == ',')) {
        // use number which has been read 
        array.push_back(number);
        i++;
    }

    int* data = new int[array.size()];
    int* d_data;
    int* d_small;
    int max;
    int* d_max;
    copy(array.begin(), array.end(), data);
    int size = sizeof(int)*array.size();

    cudaMalloc((void **) &d_data, size);
    cudaMalloc((void **) &d_max, sizeof(int));
    cudaMalloc((void **) &d_small, size);

    cudaMemcpy(d_data, data, size, cudaMemcpyHostToDevice);

    // launch the kernel
    setup<<<array.size(), BLOCK_WIDTH>>>(d_small);
    min<<<array.size(), BLOCK_WIDTH>>>(d_small, d_data);
    finish<<<array.size(), BLOCK_WIDTH>>>(d_small, d_data, d_max);

    cudaMemcpy(&max, d_max, sizeof(int), cudaMemcpyDeviceToHost);

    cudaFree(d_data);
    cudaFree(d_max);

    // force the printf()s to flush
    cudaDeviceSynchronize();

    return 0;
}
