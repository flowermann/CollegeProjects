#include <stdio.h>
#include <stdlib.h>

// function to get the dimensions of the matrix
void get_matrix_dim(int *N, int *M) {
    scanf("%d%d", N, M);
    if (*N <= 0 || *M <= 0) {
        *N = *M = 0;
    }
}

// function to get the matrix from the input
char **get_matrix(int N, int M) {
    char **matrix = calloc(N, sizeof(char *));
    if (!matrix) return NULL;

    for (int i = 0; i < N; i++) {
        matrix[i] = calloc(M, sizeof(char));
        if (!matrix[i]) {
            for (int k = 0; k < i; k++) free(matrix[k]);
            free(matrix);
            return NULL;
        }
    }

    // fill with input
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            scanf(" %c", &matrix[i][j]);
        }
    }
    return matrix;
}

// function to print the matrix
void print_matrix(char **matrix, int N, int M){
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            printf("%c", matrix[i][j]);
        }
        printf("\n");
    }
}

// function to get the number of iterations
int get_num_iterations(){
    int num_iter;
    scanf("%d", &num_iter);
    if (num_iter <= 0) return 0;
    return num_iter;
}

// function for game of life logic
void game_of_life(char **matrix, int N, int M, int num_iter){
    for (int iter = 0; iter < num_iter; iter++){
        // allocate next generation
        char **next = calloc(N, sizeof(char *));
        for (int i = 0; i < N; i++) {
            next[i] = calloc(M, sizeof(char));
        }

        // iterate over all elements of matrix
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int neighbors = 0;

                // count alive neighbors ('O' = alive, 'X' = dead)
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) continue;
                        int r = i + di;
                        int c = j + dj;
                        if (r >= 0 && r < N && c >= 0 && c < M) {
                            if (matrix[r][c] == 'O') {
                                neighbors++;
                            }
                        }
                    }
                }

                // decide next state
                if (matrix[i][j] == 'O') {
                    if (neighbors == 2 || neighbors == 3) {
                        next[i][j] = 'O'; // stays alive
                    } else {
                        next[i][j] = 'X'; // dies
                    }
                } else {
                    if (neighbors == 3) {
                        next[i][j] = 'O'; // becomes alive
                    } else {
                        next[i][j] = 'X'; // stays dead
                    }
                }
            }
        }

        // copy next into matrix
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = next[i][j];
            }
            free(next[i]);
        }
        free(next);

        // print the matrix after this iteration
        printf("ITERATION%d\n", iter);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                printf("%c", matrix[i][j]);
            }
            if (i < N - 1 || iter < num_iter - 1) {
                printf("\n");
            }
        }
    }
}
int main() {
    // getting and checking dimensions of the matrix
    int N, M;
    get_matrix_dim(&N, &M);
    if (N == 0 || M == 0) return 0;
    // getting the matrix values from the input
    char **matrix = get_matrix(N, M);
    if (matrix == NULL) return 0;

    // printing the initial matrix
    printf("INITIAL\n");
    print_matrix(matrix, N, M);

    // getting and checking the number of iterations
    int num_iter = get_num_iterations();
    if (num_iter == 0) {
        // free matrix memory if no iterations are needed
        for (int i = 0; i < N; i++) {
            free(matrix[i]);
        }
        free(matrix);
        return 0;
    }

    // simulating the game of life
    game_of_life(matrix, N, M, num_iter);

    // free matrix memory at the end
    for (int i = 0; i < N; i++) {
        free(matrix[i]);
    }
    free(matrix);

    return 0;
}