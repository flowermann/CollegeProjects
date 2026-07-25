#include <stdio.h>
#include <stdlib.h>

// funkcija za dobijanje dimenzija matrice
void get_matrix_dim(int *N, int *M) {
    scanf("%d%d", N, M);
    if (*N <= 0 || *M <= 0) {
        *N = *M = 0;
    }
}

// funkcija za dobijanje matrice sa ulaza
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

    // popunjavanje sa ulaza
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            scanf(" %c", &matrix[i][j]);
        }
    }
    return matrix;
}

// funkcija za štampanje matrice
void print_matrix(char **matrix, int N, int M, char* title){
    printf("%s\n", title);
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            printf("%c", matrix[i][j]);
        }
        // Dodaje novi red nakon svakog reda, osim poslednjeg
        if (i < N - 1) {
            printf("\n");
        }
    }
}

// funkcija za dobijanje broja iteracija
int get_num_iterations(){
    int num_iter;
    scanf("%d", &num_iter);
    if (num_iter <= 0) return 0;
    return num_iter;
}

// funkcija za logiku igre života
void game_of_life(char **matrix, int N, int M, int num_iter){
    for (int iter = 0; iter < num_iter; iter++){
        // alociranje sledeće generacije
        char **next = calloc(N, sizeof(char *));
        for (int i = 0; i < N; i++) {
            next[i] = calloc(M, sizeof(char));
        }

        // iteriranje kroz sve elemente matrice
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int neighbors = 0;

                // brojanje živih komšija ('O' = živ, 'X' = mrtav)
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

                // odlučivanje o sledećem stanju
                if (matrix[i][j] == 'O') {
                    if (neighbors == 2 || neighbors == 3) {
                        next[i][j] = 'O'; // ostaje živ
                    } else {
                        next[i][j] = 'X'; // umire
                    }
                } else {
                    if (neighbors == 3) {
                        next[i][j] = 'O'; // postaje živ
                    } else {
                        next[i][j] = 'X'; // ostaje mrtav
                    }
                }
            }
        }

        // kopiranje 'next' u 'matrix'
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                matrix[i][j] = next[i][j];
            }
        }

        char buffer[20];
        sprintf(buffer, "ITERATION%d", iter);
        
        // Dodajemo prazan red pre svake iteracije.
        printf("\n");
        print_matrix(matrix, N, M, buffer);
        

        // oslobađanje 'next' memorije
        for (int i = 0; i < N; i++) {
            free(next[i]);
        }
        free(next);
    }
}

int main() {
    // dobijanje i provera dimenzija matrice
    int N, M;
    get_matrix_dim(&N, &M);
    if (N == 0 || M == 0) return 0;

    // dobijanje vrednosti matrice sa ulaza
    char **matrix = get_matrix(N, M);
    if (matrix == NULL) return 0;

    // štampanje početne matrice
    print_matrix(matrix, N, M, "INITIAL");
    
    // dobijanje i provera broja iteracija
    int num_iter = get_num_iterations();
    if (num_iter == 0) return 0;

    // simulacija igre života
    game_of_life(matrix, N, M, num_iter);

    // oslobađanje memorije
    for (int i = 0; i < N; i++) {
        free(matrix[i]);
    }
    free(matrix);

    return 0;
}