#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// function to get matrix dimensions
int get_matrix_dim() {
    int N;
    scanf("%d", &N);
    if (N <= 0) return 0;
    return N;
}

// function to get the matrix from the input
int **get_matrix(int N) {
    int **matrix = calloc(N, sizeof(int *));
    if (!matrix) {
        return NULL;
    }
    for (int i = 0; i < N; i++) {
        matrix[i] = calloc(N, sizeof(int));
        if (!matrix[i]) {
            for (int k = 0; k < i; k++) {
                free(matrix[k]);
            }
            free(matrix);
            return NULL;
        }
    }
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            scanf("%d", &matrix[i][j]);
        }
    }
    return matrix;
}

// checks if the matrix values are in correct range, returns validity flag
int check_valid_values(int **matrix, int N) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (matrix[i][j] <= 0 || matrix[i][j] > N) return 0;
        }
    }
    return 1;
}

// function to print the matrix
void print_matrix(int **matrix, int N) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            printf("%d", matrix[i][j]);
            if (j < N - 1) printf(" ");
        }
        printf("\n");
    }
}

// check all rows
int check_rows(int **matrix, int S) {
    int *seen = malloc((S + 1) * sizeof(int));
    if (!seen) return 0;
    int valid = 1;
    for (int row = 0; row < S; row++) {
        for (int num = 1; num <= S; num++) {
            seen[num] = 0;
        }
        for (int col = 0; col < S; col++) {
            int value = matrix[row][col];
            if (seen[value]) {
                valid = 0;
            }
            seen[value] = 1;
        }
    }
    free(seen);
    return valid;
}

// check all columns
int check_columns(int **matrix, int S) {
    int *seen = malloc((S + 1) * sizeof(int));
    if (!seen) return 0;
    int valid = 1;
    for (int col = 0; col < S; col++) {
        for (int num = 1; num <= S; num++) {
            seen[num] = 0;
        }
        for (int row = 0; row < S; row++) {
            int value = matrix[row][col];
            if (seen[value]) {
                valid = 0;
            }
            seen[value] = 1;
        }
    }
    free(seen);
    return valid;
}

// check NxN regions
int check_regions(int **matrix, int S) {
    int regionSize = (int)sqrt(S);
    int *seen = malloc((S + 1) * sizeof(int));
    if (!seen) return 0;
    int valid = 1;
    for (int regionRow = 0; regionRow < regionSize; regionRow++) {
        for (int regionCol = 0; regionCol < regionSize; regionCol++) {
            for (int num = 1; num <= S; num++) {
                seen[num] = 0;
            }
            int startRow = regionRow * regionSize;
            int startCol = regionCol * regionSize;
            for (int rowOffset = 0; rowOffset < regionSize; rowOffset++) {
                for (int colOffset = 0; colOffset < regionSize; colOffset++) {
                    int row = startRow + rowOffset;
                    int col = startCol + colOffset;
                    int value = matrix[row][col];
                    if (seen[value]) {
                        valid = 0;
                    }
                    seen[value] = 1;
                }
            }
        }
    }
    free(seen);
    return valid;
}

// final function to check the solution
void check_solution(int **matrix, int S) {
    int ok_cols = check_columns(matrix, S);
    int ok_rows = check_rows(matrix, S);
    int ok_regs = check_regions(matrix, S);
    int all_ok = 1;

    if (!ok_cols) {
        printf("LOSE KOLONE");
        all_ok = 0;
    }
    if (!ok_rows) {
        if(!all_ok) printf("\n");
        printf("LOSI REDOVI");
        all_ok = 0;
    }
    if (!ok_regs) {
        if(!all_ok) printf("\n");
        printf("LOSI REGIONI");
        all_ok = 0;
    }

    if (all_ok) {
        printf("VALIDNO RESENJE");
    }
}

int main() {
    int N = get_matrix_dim();
    if (N == 0) return 0;
    
    int **matrix = get_matrix(N * N);
    if (matrix == NULL) return 1;

    int valid_values = check_valid_values(matrix, N * N);
    if (!valid_values) {
        printf("LOS OPSEG");
    } else {
        print_matrix(matrix, N * N);
        check_solution(matrix, N * N);
    }

    // free allocated memory
    for (int i = 0; i < N * N; i++) {
        free(matrix[i]);
    }
    free(matrix);
    
    return 0;
}