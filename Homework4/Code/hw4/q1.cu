#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cuda.h>

using namespace std;

#define THREADS 256

__global__ void setup(int* small, int n) {
    int thid = blockIdx.x * blockDim.x + threadIdx.x;
    if(thid < n) {
        small[thid] = 1;
    }
}

__global__ void min(int* small, int* data, int n) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    for(int j = 0; j < n; j++) {
        if(i < n) {
            if(data[j] < data[i] && i != j) {
                small[i] = 0;
            }
        }
    }
}

__global__ void finish(int* small, int* data, int* max, int n) {
    int thid = blockIdx.x * blockDim.x + threadIdx.x;
    if(thid < n) {
        if(small[thid] == 1) {
            *max = data[thid];
        }
    }
}

__global__ void last_digits(int* mod, int* data, int n) {
    int thid = blockIdx.x * blockDim.x + threadIdx.x;
    if(thid < n) {
        mod[thid] = data[thid]%10;
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
    int* d_small;
    int max;
    int* d_max;
    int* d_mod;
    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
    }

    int size = sizeof(int)*array.size();

    cudaMalloc((void **) &d_data, size);
    cudaMalloc((void **) &d_max, sizeof(int));
    cudaMalloc((void **) &d_small, size);
    cudaMalloc((void **) &d_mod, size);

    cudaMemcpy(d_data, data, size, cudaMemcpyHostToDevice);

    int sizing = array.size()/THREADS;
    if(array.size()%THREADS > 0) {
        sizing++;
    }
    // launch the kernel
    setup<<<sizing, THREADS>>>(d_small, (int) array.size());
    min<<<sizing, THREADS>>>(d_small, d_data, (int) array.size());
    finish<<<sizing, THREADS>>>(d_small, d_data, d_max, (int) array.size());
    cudaMemcpy(&max, d_max, sizeof(int), cudaMemcpyDeviceToHost);

    last_digits<<<sizing, THREADS>>>(d_mod, d_data, array.size());
    cudaMemcpy(mod, d_mod, size, cudaMemcpyDeviceToHost);

    FILE *fp = fopen("q1b.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < array.size() - 1; a++) {
            fprintf(fp, "%d, ", mod[a]);
        }
        fprintf(fp, "%d", mod[array.size() - 1]);
        fclose(fp);
    }

    fp = fopen("q1a.txt", "w");
    if(fp != NULL) {
        fprintf(fp, "%d", max);
        fclose(fp);
    }


    printf("\n");
    printf("%d ", max);
    printf("\n");

    cudaFree(d_data);
    cudaFree(d_max);
    cudaFree(d_mod);

    // force the printf()s to flush
    cudaDeviceSynchronize();


    return 0;
}
