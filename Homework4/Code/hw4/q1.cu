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

__global__ void min(int* iB, int* max) {
    printf("Thread %d read %d\n", blockIdx.x, iB[blockIdx.x]);
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

    int* iB = new int[array.size()];
    int* d_iB;
    int max;
    int * d_max;
    copy(array.begin(), array.end(), iB);
    for(int a = 0; a < array.size(); a++) {
        printf("%d ", iB[a]);
    }

    int size = sizeof(int)*array.size();
    printf("%d", size);

    cudaMalloc((void **) &d_iB, size);
    cudaMalloc((void **) &d_max, sizeof(int));

    cudaMemcpy(d_iB, iB, size, cudaMemcpyHostToDevice);

    // launch the kernel
    min<<<array.size(), BLOCK_WIDTH>>>(d_iB, d_max);

    cudaMemcpy(&max, d_max, sizeof(int), cudaMemcpyDeviceToHost);

    cudaFree(d_iB);
    cudaFree(d_max);

    // force the printf()s to flush
    cudaDeviceSynchronize();

    return 0;
}
