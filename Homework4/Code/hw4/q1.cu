#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
using namespace std;

#define NUM_BLOCKS 16
#define BLOCK_WIDTH 1

__global__ void min() {
    printf("Hello world! I'm a thread in block %d\n", blockIdx.x);
}


int main(int argc,char **argv) {
    vector<int> array;
    int i = 0;

    ifstream file( "inp2.txt" );
    int number;
    char delimiter;
    while((file >> number >> delimiter) && (delimiter == ',')) {
        // use number which has been read 
        printf("%d ", number);
        array.push_back(number);
        i++;
    }

    // launch the kernel
    min<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    cudaDeviceSynchronize();

    return 0;
}
