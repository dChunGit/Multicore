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

// __global__ void min() {
//     printf("Hello world! I'm a thread in block %d\n", blockIdx.x);
// }

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

    int* iB = new int[array.size()];
    int * d_iB;

    copy(array.begin(), array.end(), iB);


    int size = sizeof(int)*array.size();
    for(int a = 0; a < array.size(); a++) {
        printf("%d ", iB[a]);
    }

    printf("/n");
    printf("%d", array.size());
    printf("/n");
    // cudaMalloc((void **) &d_iB, size);

    // cudaMemcpy(d_iB, &iB, size, cudaMemcpyHostToDevice);

    // launch the kernel
    // min<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    // cudaDeviceSynchronize();

    return 0;
}
