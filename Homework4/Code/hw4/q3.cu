#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cuda.h>

using namespace std;

#define THREADS 20

__global__ void setup(int* data, int* odd, int* count, int n) {
	int thid = blockIdx.x*THREADS + threadIdx.x;
	if(thid >= n) {
		return;
	}
	
	if(data[thid]%2 == 1) {
	    odd[thid] = 1;
	    atomicAdd(count, 1);
	} else odd[thid] = 0;
}

__global__ void parallelPrefix(int* odd, int* ppref, int n) {
	int thid = blockIdx.x*THREADS + threadIdx.x;
	if(thid >= n) {
		return;
	}
	int val = 0;
	ppref[thid] = odd[thid];
	printf("%d ", ppref[thid]);
	__syncthreads();

	for(int i = 1; i < n; i *= 2) {
		if(thid >= i) {
			val = ppref[thid - i];
		}
		__syncthreads();

		if(thid >= i) {
			ppref[thid] += val;
		}
		__syncthreads();
	}
}

__global__ void finish(int* odd, int* ppref, int* results, int* data, int n) {
	int thid = blockIdx.x*THREADS + threadIdx.x;
	if(thid >= n) {
		return;
	}
	
	if(odd[thid] == 1) {
		results[ppref[thid] - 1] = data[thid];
	}
}

int main(int argc,char **argv) {
    vector<int> array;
    int i = 0;

    ifstream file( "inp2.txt" );
    int number;
    while(file>>number) {
        array.push_back(number); 
        i++;
        if (file.peek() == ',')
            file.ignore();
    }

    int* data = new int[array.size()];
    int* ppref = new int[array.size()];
    int* odd = new int[array.size()];
    int count;
    int* d_data;
    int* d_odd;
    int* d_ppref;
    int* d_count;

    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
        printf("%d ", data[a]);
    }
    printf("\n");

    int size = sizeof(int)*array.size();

    cudaMalloc((void **) &d_data, size);
    cudaMalloc((void **) &d_odd, size);
    cudaMalloc((void **) &d_ppref, size);
    cudaMalloc((void **) &d_count, sizeof(int));

    cudaMemcpy(d_data, data, size, cudaMemcpyHostToDevice);  

    // launch the kernel
    int blocks = array.size()/THREADS;
    if(array.size()%THREADS > 0) {
    	blocks += 1;
    }

    setup<<<blocks, THREADS>>>(d_data, d_odd, d_count, array.size());
    //get number of odds
    cudaMemcpy(&count, d_count, sizeof(int), cudaMemcpyDeviceToHost);

    //do parallel prefix on odd array to find distance from the start
    parallelPrefix<<<blocks, THREADS>>>(d_odd, d_ppref, array.size());
    // force the printf()s to flush
    cudaDeviceSynchronize();

    cudaMemcpy(ppref, d_ppref, size, cudaMemcpyDeviceToHost); 
    printf("\n");
    for(int a = 0; a < array.size(); a++) {
    	printf("(%d:%d), ", ppref[a], data[a]);
    }

    //create array, if odd from small, copy into location of array
    // int* results = new int[count];
    // int* d_results;
    // cudaMalloc((void **) &d_results, sizeof(int)*count);
    // finish<<<blocks, THREADS>>>(d_odd, d_ppref, d_results, d_data, array.size());
    // cudaMemcpy(results, d_results, sizeof(int)*count, cudaMemcpyDeviceToHost);
    // printf("\n");
    // for(int a = 0; a < count; a++) {
    //     printf("%d ", results[a]);
    // }

    // cudaFree(d_data);
    // cudaFree(d_odd);
    cudaFree(d_ppref);
    cudaFree(d_count);
    // cudaFree(d_results);

    // force the printf()s to flush
    cudaDeviceSynchronize();


    return 0;
}
