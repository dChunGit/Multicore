#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
// #include <cuda.h>

using namespace std;

#define NUM_BLOCKS 16
#define BLOCK_WIDTH 1

// __global__ void min() {
//     printf("Hello world! I'm a thread in block %d\n", blockIdx.x);
// }

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

    for(int a = 0; a < array.size(); a++) {
        printf("%d ", array[a]);
    }
    printf("\n");

    int* iB = new int[array.size()];
    int * d_iB;

    copy(array.begin(), array.end(), iB);

    int size = sizeof(int)*array.size();
    for(int a = 0; a < array.size(); a++) {
        printf("%d ", iB[a]);
    }

    printf("\n");
    printf("%d, %d", array.size(), i);
    printf("\n");

    FILE *fp = fopen("q1a.txt", "w");
    if(fp != NULL) {
        for(int a = 0; a < array.size() - 1; a++) {
            fprintf(fp, "%d, ", iB[a]);
        }
        fprintf(fp, "%d", iB[array.size() - 1]);
    }
    // cudaMalloc((void **) &d_iB, size);

    // cudaMemcpy(d_iB, &iB, size, cudaMemcpyHostToDevice);

    // launch the kernel
    // min<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    // cudaDeviceSynchronize();

    return 0;
}
