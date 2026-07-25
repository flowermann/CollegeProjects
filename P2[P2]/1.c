#include <stdio.h>
#include <stdlib.h>

// function to check the number of buyers

int check_buyers(){
    int N;
    scanf("%d", &N);
    if (N <= 0) return 0;
    return N;
}

// loading and checking all of the buyers, returns pointer to array of buyers
int *load_check_buyers(int N) {
    int *p = malloc(N * sizeof(int));
    if (!p) {
        return NULL;
    }
    int valid_array = 1;
    for (int i = 0; i < N; i++) {
        scanf("%d", &p[i]);
        if (p[i] <= 0) {
            valid_array = 0;
        }
    }
    if (!valid_array) {
        free(p);
        return NULL;
    }
    return p;
}

// function to check the number of cash registers
int check_num_registers(){
    int M;
    scanf("%d", &M);
    if (M <= 0) return 0;
    return M;
}

// Find index of the smallest element in an array
int find_min_index(int *arr, int size) {
    if (size <= 0) return 0;  // invalid input

    int min_index = 0;
    int min_value = arr[0];

    for (int i = 1; i < size; i++) {
        if (arr[i] < min_value) {
            min_value = arr[i];
            min_index = i;
        }
    }
    return min_index;
}

// distribute buyers to registers and print the result
void distribute_and_print(int *buyers, int num_buyers, int num_registers) {

    // total number of items per register
    int *register_load = calloc(num_registers, sizeof(int));

    // matrix to store which buyers go to which register
    int **register_assignments = malloc(num_registers * sizeof(int*));
    // used for indexing buyer position for each register since we have a matrix and need a second index
    int *register_counts = calloc(num_registers, sizeof(int));

    for (int i = 0; i < num_registers; i++) {
        register_assignments[i] = malloc(num_buyers * sizeof(int));
    }

    for (int i = 0; i < num_buyers; i++) {
        // find the register with the smallest current load
        int reg_idx = find_min_index(register_load, num_registers);

        register_assignments[reg_idx][register_counts[reg_idx]] = buyers[i];
        register_counts[reg_idx]++;
        register_load[reg_idx] += buyers[i];
    }

    // print the buyers assigned to each register
    int first_line = 1;
    for (int r = 0; r < num_registers; r++) {
        if (register_counts[r] > 0) {
            if (!first_line) {
                printf("\n");
            }
            for (int b = 0; b < register_counts[r]; b++) {
                printf("%d", register_assignments[r][b]);
                if (b < register_counts[r] - 1) printf(" ");
            }
            first_line = 0;
        }
    }

    // free allocated memory
    for (int r = 0; r < num_registers; r++) {
        free(register_assignments[r]);
    }
    free(register_assignments);
    free(register_counts);
    free(register_load);
}

int main(){
    // checking the number of buyers
    int N = check_buyers();
    if (N == 0) return 0;
    // checking the buyer values
    int *buyers = load_check_buyers(N);
    if (buyers == NULL) return 0;
    // check the number of cash_registers
    int M = check_num_registers();
    if ( M == 0) {
        free(buyers);
        return 0;
    }
    // print the final result matrix
    distribute_and_print(buyers, N, M);
    // free all allocated memory in the end
    free(buyers);
    return 0;
}