#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cuda.h>

using namespace std;

#define THREADS 64

__global__ void setup(int* data, int* odd, int* count, int n) {
	int thid = blockIdx.x*blockDim.x + threadIdx.x;
	if(thid >= n) {
		return;
	}
	
	if(data[thid]%2 == 1) {
	    odd[thid] = 1;
	    atomicAdd(count, 1);
	} else odd[thid] = 0;
}

__global__ void parallelPrefix(int* odd, int* ppref, int* offset, int n) {
	extern __shared__ int prefs[];
	int thid = blockIdx.x*blockDim.x + threadIdx.x;
	if(thid >= n) {
		return;
	}
	prefs[threadIdx.x] = odd[thid];

	__syncthreads();

	int val = 0;
	for(int i = 1; i < THREADS; i *= 2) {
		if(threadIdx.x >= i) {
			val = prefs[threadIdx.x - i];
		}
		__syncthreads();

		if(threadIdx.x >= i) {
			prefs[threadIdx.x] += val;
		}
		__syncthreads();
	}
	ppref[thid] = prefs[threadIdx.x];
	if(threadIdx.x == THREADS - 1) {
		offset[blockIdx.x] = ppref[thid];
	}
}

__global__ void sum_reduce(int* offset, int n) {
	extern __shared__ int prefs[];
	int thid = threadIdx.x;

	printf("\n");
	prefs[thid] = offset[thid];
	__syncthreads();

	int val = 0;
	for(int i = 1; i < n; i *= 2) {
		if(thid >= i) {
			val = prefs[thid - i];
		}
		// printf("%d \n", val);
		__syncthreads();

		if(thid >= i) {
			prefs[thid] += val;
		}
		__syncthreads();
	}
	offset[thid] = prefs[thid];
}


__global__ void concat(int* offset, int*ppref) {
	int thid = blockIdx.x*blockDim.x + threadIdx.x;
	if(blockIdx.x > 0) {
		ppref[thid] += offset[blockIdx.x - 1];
	}
}

__global__ void finish(int* odd, int* ppref, int* results, int* data, int n) {
	int thid = blockIdx.x*blockDim.x + threadIdx.x;
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

    ifstream file( "inp.txt" );
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
    int* d_offset;

    for(int a = 0; a < array.size(); a++) {
        data[a] = array[a];
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
    cudaMalloc((void **) &d_offset, sizeof(int)*blocks);
    int* offset = new int[blocks];

    setup<<<blocks, THREADS>>>(d_data, d_odd, d_count, array.size());
    //get number of odds
    cudaMemcpy(&count, d_count, sizeof(int), cudaMemcpyDeviceToHost);

    //do parallel prefix on odd array to find distance from the start
    parallelPrefix<<<blocks, THREADS, sizeof(int)*THREADS>>>(d_odd, d_ppref, d_offset, array.size());
    cudaMemcpy(offset, d_offset, sizeof(int)*blocks, cudaMemcpyDeviceToHost); 

    sum_reduce<<<1, blocks, sizeof(int)*blocks>>>(d_offset, blocks);
    concat<<<blocks, THREADS>>>(d_offset, d_ppref);

    cudaMemcpy(ppref, d_ppref, size, cudaMemcpyDeviceToHost); 

    //create array, if odd from small, copy into location of array
    int* results = new int[count];
    int* d_results;
    cudaMalloc((void **) &d_results, sizeof(int)*count);
    finish<<<blocks, THREADS>>>(d_odd, d_ppref, d_results, d_data, array.size());
    cudaMemcpy(results, d_results, sizeof(int)*count, cudaMemcpyDeviceToHost);
    // printf("\n");
    // for(int a = 0; a < count; a++) {
    //     printf("%d ", results[a]);
    // }

    FILE *fp = fopen("q3.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < count; a++) {
            fprintf(fp, "%d", results[a]);
            if(a + 1 < count) {
                fprintf(fp, ", ");
            }
        }
        fclose(fp);
    }

    cudaFree(d_data);
    cudaFree(d_odd);
    cudaFree(d_ppref);
    cudaFree(d_count);
    cudaFree(d_results);

    // force the printf()s to flush
    cudaDeviceSynchronize();


    return 0;
}
