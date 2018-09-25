#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>
#include <fstream>

using namespace std;
int omp_get_thread_num();
void MatrixMult(char file1[],char file2[],int T)
{
    int file1_row = 0, file1_col = 0, file2_row = 0, file2_col = 0;
    double** m1;
    double** m2;
    ifstream inFile;
    inFile.open(file1);

    if(inFile.is_open()) {
        inFile >> file1_row;
        inFile >> file1_col;
        printf("%i, %i\n", file1_row, file1_col);

        m1 = new double*[file1_row];
        for(int a = 0; a < file1_row; a++) {
            m1[a] = new double[file1_col];
        }
        int row = 0, col = 0;
        double x = 0;
        while(inFile >> x) {
            printf("%f\n", x);
            printf("%i, %i\n", row, col);
            m1[row][col] = x;
            col++;
            if(col == file1_col) {
                col = 0;
                row++;
            }
        }
    }
    inFile.close();
    inFile.open(file2);

    if(inFile.is_open()) {
        inFile >> file2_row;
        inFile >> file2_col;
        printf("%i, %i\n", file2_row, file2_col);

        m2 = new double*[file2_row];
        for(int a = 0; a < file2_row; a++) {
            m2[a] = new double[file2_col];
        }
        int row = 0, col = 0;
        double x = 0;
        while(inFile >> x) {
            printf("%f\n", x);
            printf("%i, %i\n", row, col);
            m2[row][col] = x;
            col++;
            if(col == file2_col) {
                col = 0;
                row++;
            }
        }
    }
    inFile.close();

    double** output = new double*[file1_row];
    for(int o = 0; o < file1_row; o++) {
        output[o] = new double[file2_col];
    }

    #pragma omp parallel num_threads(T)
    {
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

        printf("Mine: %i, Number starting: %i, Number to do: %i\n", my_num, end, number_rows);
    }

    for(int a = 0; a < file1_row; a++) {
        for(int b = 0; b < file2_col; b++) {
            printf("%f ", output[a][b]);
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


