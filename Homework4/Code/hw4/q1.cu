#include <stdio.h>
#include <vector>
using namespace std;

#define NUM_BLOCKS 16
#define BLOCK_WIDTH 1

__global__ void hello() {
    printf("Hello world! I'm a thread in block %d\n", blockIdx.x);
}


int main(int argc,char **argv) {
    vector<int> array;
    int x, i = 0;

    FILE* inFile = fopen(file1, "r");
    if(inFile != NULL) {
        while(fscanf(inFile, "%lf", &x) != EOF) {
            array[i] = x;
            i++;
        }
    }
    fclose(inFile);
    
    for(int a = 0; a < i - 1; a++) {
        printf("%d, ", array[a]);
    }

    // launch the kernel
    hello<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    cudaDeviceSynchronize();

    printf("That's all!\n");

    return 0;
}
