#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>

void MatrixMult(char file1[],char file2[],int T)
{
    int file1_row = 0, file1_col = 0, file2_row = 0, file2_col = 0;
    double** m1;
    double** m2;
    
    // Open files and read into 2d arrays
    FILE* inFile = fopen("inp.txt", "r");
    if(inFile != NULL) {
        fscanf(inFile, "%d", &file1_row);
        fscanf(inFile, "%d", &file1_col);

        m1 = new double*[file1_row];
        for(int a = 0; a < file1_row; a++) {
            m1[a] = new double[file1_col];
        }
        double x = 0;
        int row = 0, col = 0;

        while(fscanf(inFile, "%lf", &x) != EOF) {
            m1[row][col] = x;
            col++;
            if(col == file1_col) {
                col = 0;
                row++;
            }
        }
    }
    fclose(inFile);
    inFile = fopen(file2, "r");
    if(inFile != NULL) {
        fscanf(inFile, "%d", &file2_row);
        fscanf(inFile, "%d", &file2_col);

        m2 = new double*[file2_row];
        for(int a = 0; a < file2_row; a++) {
            m2[a] = new double[file2_col];
        }
        double x = 0;
        int row = 0, col = 0;

        while(fscanf(inFile, "%lf", &x) != EOF) {
            m2[row][col] = x;
            col++;
            if(col == file2_col) {
                col = 0;
                row++;
            }
        }
    }
    fclose(inFile);

    // create output array
    double** output = new double*[file1_row];
    for(int o = 0; o < file1_row; o++) {
        output[o] = new double[file2_col];
    }

    #pragma omp parallel num_threads(T)
    { 
        // Split matrix into T threads, each taking a block of matrix rows to compute
        int my_num = omp_get_thread_num();
        int number_rows = file1_row/omp_get_num_threads();
        int end = number_rows * my_num;
        if(my_num == omp_get_num_threads() - 1) {
            number_rows += file1_row%omp_get_num_threads();
        }

        for(int row = end; row < end + number_rows; row++) {
            for(int col = 0; col < file2_col; col++) {
                for(int row1 = 0; row1 < file2_row; row1++) {
                    output[row][col] += m1[row][row1] * m2[row1][col];
                }
            }
        }
    }
    
    printf("%i %i \n", file1_row, file2_col);
    for(int a = 0; a < file1_row; a++) {
        for(int b = 0; b < file2_col; b++) {
            printf("%lf  ", output[a][b]);
        }
        printf("\n");
    }
}

int main(int argc, char *argv[])
{
  char *file1, *file2;
  file1=argv[1];
  file2=argv[2];
  int T=atoi(argv[3]);
  MatrixMult(file1,file2,T);
}


