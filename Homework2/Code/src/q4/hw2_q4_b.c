#include <stdio.h>
#include <stdlib.h>
#include <omp.h>


double Get_Random() {
    return (36) * ( (double)rand() / (double)RAND_MAX );
}

double MonteCarlo(int s)
{
    //Write your code here
    int num_threads = 3;
    int num_points = s/num_threads;
    int overflow = s%num_threads;
    double count = 0;


    #pragma omp parallel num_threads(num_threads)
    {
        for(int a = 0; a < num_points; a++) {
            double x = Get_Random(), y = Get_Random();
            double value = (x*x) + (y*y);
            if(value < 1296) {
                #pragma omp atomic
                count++;
            }
        }
        if(omp_get_thread_num() < overflow) {
            double x = Get_Random(), y = Get_Random();
            double value = (x*x) + (y*y);
            if(value < 1296) {
                #pragma omp atomic
                count++;
            }
        }
    }

    return (count/s)*4;
}

int main()
{
    double pi;
    pi=MonteCarlo(100000000);
    printf("Value of pi is: %lf\n",pi);
}



