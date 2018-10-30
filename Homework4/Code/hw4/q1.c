#include <stdio.h>
#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
using namespace std;

#define NUM_BLOCKS 16
#define BLOCK_WIDTH 1

// __global__ void hello() {
//     printf("Hello world! I'm a thread in block %d\n", blockIdx.x);
// }


int main(int argc,char **argv) {
    vector<int> array;
    int x = 0, i = 0;

    // FILE* inFile = fopen("inp2.txt", "r");
    // if(inFile != NULL) {
    //     while(inFile>>x) {
    //         array.push_back(x); 
    //         i++;
    //     }

    //     // while(fscanf(inFile, "%d", &x) != EOF) {
    //     //     printf("%d ", x);
    //     //     array.push_back(x);
    //     //     i++;
    //     // }
    // }
    // // fclose(inFile);
    // ifstream inFile;
    // stringstream stream;
    // inFile.open("inp2.txt", ifstream::in);
    // if(inFile.is_open()) {
    //     while(inFile.good()) {
    //         inFile.getline(inFile, x, ', ');
    //         printf("%d ", x);
    //         i++;
    //     }
    // }
    ifstream file( "inp2.txt" ) ;
    int number;
    char delimiter;
    while((file >> number >> delimiter) && (delimiter == ',')) {
        // use number which has been read 
        printf("%d ", number);
        array.push_back(number);
        i++;
    }

    printf("\n");

    printf("\n");
    
    for(int a = 0; a < i; a++) {
        printf("%d ", array[a]);
    }

    printf("\n");

    // launch the kernel
    // hello<<<NUM_BLOCKS, BLOCK_WIDTH>>>();

    // force the printf()s to flush
    // cudaDeviceSynchronize();

    return 0;
}
