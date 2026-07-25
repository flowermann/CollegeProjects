#include <stdio.h>
#include <stdlib.h>

// function to get array length
int get_length(){
    int N;
    scanf("%d", &N);
    if (N <= 0) return 0;
    return N;
}
// function to get array values
int *get_array(int N){
    int *arr = calloc(N, sizeof(int));
    if (!arr) return NULL;
    for (int i = 0; i < N; i++){
        scanf("%d", &arr[i]);
    }
    return arr;
}
//function to check valid call numbers (0-99)
int check_num_calls(int *arr, int N){
    for (int i = 0; i < N; i++){
        if (arr[i] < 0 || arr[i] > 99) return 0;
    }
    return 1;
}
// function to check valid call lengths (>0)
int check_call_len(int *arr, int N){
    for (int i = 0; i < N; i++){
        if (arr[i] <= 0) return 0;
    }
    return 1;
}
// function to print array
void print_array(int *arr, int N){
    for (int i = 0; i < N; i++){
        printf("%d", arr[i]);
        if (i < N - 1) {
            printf(" ");
        }
    }
    printf("\n");
}
// function to calculate impulses for all calls
int *calculate_impulses(int *call_len, int N, int period){
    int *impulses = calloc(N, sizeof(int));
    if (!impulses) return NULL;
    for (int i = 0; i < N; i++){
        int x = call_len[i] / period;
        if (call_len[i] % period != 0) x++;
        impulses[i] = x;
    }
    return impulses;
}
// get unique values and their first occurrences
int *get_unique_ids(int *num_of_calls, int N, int *unique_size){
    int *temp = calloc(N, sizeof(int));
    if (!temp) return NULL;
    int count = 0;
    for (int i = 0; i < N; i++){
        int seen = 0;
        for (int j = 0; j < count; j++){
            if (temp[j] == num_of_calls[i]){
                seen = 1;
                break;
            }
        }
        if (!seen){
            temp[count++] = num_of_calls[i];
        }
    }
    // sort unique IDs
    for (int i = 0; i < count - 1; i++){
        for (int j = 0; j < count - i - 1; j++){
            if (temp[j] > temp[j + 1]){
                int tmp = temp[j];
                temp[j] = temp[j + 1];
                temp[j + 1] = tmp;
            }
        }
    }
    *unique_size = count;
    return temp;
}
int main(){
    // get dimensions of array
    int N = get_length();
    if (N == 0) return 0;
    // get both arrays from input
    int *num_of_calls = get_array(N);
    if (num_of_calls == NULL) return 0;
    int *call_len = get_array(N);
    if (call_len == NULL) {
        free(num_of_calls);
        return 0;
    }
    // check elements of both arrays
    if (!check_num_calls(num_of_calls, N) || !check_call_len(call_len, N)) return 0;
    // get period from input
    int period = get_length();
    if (!period) {
        free(num_of_calls);
        free(call_len);
        return 0;
    }

    // Ispis prvog niza
    print_array(num_of_calls, N);

    // Ispis drugog niza
    print_array(call_len, N);

    // calculate impulses for all calls
    int *all_impulses = calculate_impulses(call_len, N, period);
    if (all_impulses == NULL) {
        free(num_of_calls);
        free(call_len);
        return 0;
    }

    // compute total impulses
    int total_impulses = 0;
    for (int i = 0; i < N; i++) total_impulses += all_impulses[i];

    // print total impulses
    printf("%d\n", total_impulses);

    // get unique IDs sorted
    int unique_size = 0;
    int *unique_ids = get_unique_ids(num_of_calls, N, &unique_size);
    if (unique_ids == NULL){
        free(num_of_calls);
        free(call_len);
        free(all_impulses);
        return 0;
    }

    // print unique ID and their total impulses
    for (int i = 0; i < unique_size; i++){
        int id = unique_ids[i];
        int sum_impulses = 0;
        for (int j = 0; j < N; j++){
            if (num_of_calls[j] == id){
                sum_impulses += all_impulses[j];
            }
        }
        printf("%d %d", id, sum_impulses);
        if (i < unique_size - 1) {
            printf("\n");
        }
    }

    // free memory
    free(num_of_calls);
    free(call_len);
    free(all_impulses);
    free(unique_ids);

    return 0;
}