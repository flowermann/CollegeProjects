#include <iostream>
#include <cmath>

using namespace std;

// RNG unit implemented using BBS algorithm with parity bit 

double generate_random_unit(unsigned long long& x, unsigned long long n_bbs)
{
    const int BITS = 16;
    unsigned int value = 0;

    for (int i = 0; i < BITS; i++)
    {
        x = (x * x) % n_bbs;
        value = (value << 1) | (x & 1);
    }
    return (double)value / (double)(1U << BITS);
}

int random_int_range(unsigned long long& x,
    unsigned long long n_bbs,
    int min,
    int max)
{
    double u = generate_random_unit(x, n_bbs);
    return min + (int)(u * (double)(max - min + 1));
}

double random_distance(unsigned long long& x,
    unsigned long long n_bbs)
{
    double u = generate_random_unit(x, n_bbs);
    double d = 0.5 + u * (30.0 - 0.5);
    return ((int)(d * 10)) / 10.0;
}

// Graph structures used to implement the sensor network

struct Sensor
{
    int sensor_id;
    int sensor_speed;
};

struct AdjNode
{
    int dest;
    double weight;
    AdjNode* next;
};

struct Graph
{
    int n;
    AdjNode** adj;
};

// Constructing the graph

Graph* create_graph(int n)
{
    Graph* g = new Graph;
    g->n = n;
    g->adj = new AdjNode * [n];
    for (int i = 0; i < n; i++)
        g->adj[i] = nullptr;
    return g;
}

void add_edge(Graph* g, int src, int dest, double weight)
{
    AdjNode* node = new AdjNode;
    node->dest = dest;
    node->weight = weight;
    node->next = g->adj[src];
    g->adj[src] = node;
}

// Connectivity Matrix 

int** create_matrix(int n)
{
    int** m = new int* [n];
    for (int i = 0; i < n; i++)
    {
        m[i] = new int[n];
        for (int j = 0; j < n; j++)
            m[i][j] = 0;
    }
    return m;
}

void print_matrix(int** m, int n)
{
    cout << "\nConnectivity matrix:\n";
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
            cout << m[i][j] << " ";
        cout << endl;
    }
}

// Initial graph generation 

void generate_random_graph(Graph* g,
    Sensor* sensors,
    int** matrix,
    double p,
    unsigned long long& x,
    unsigned long long n_bbs)
{
    for (int i = 0; i < g->n; i++)
    {
        for (int j = 0; j < g->n; j++)
        {
            if (i == j) continue;
            double u = generate_random_unit(x, n_bbs);
            if (u < p)
            {
                double d = random_distance(x, n_bbs);
                int vi = sensors[i].sensor_speed;
                int vj = sensors[j].sensor_speed;
                double w = d + 100 - (vi < vj ? vi : vj);
                add_edge(g, i, j, w);
                matrix[i][j] = 1;
            }
        }
    }
}

//  Printing the graph

void print_graph(Graph* g, Sensor* sensors)
{
    cout << "\nGraph (adjacency list):\n";
    for (int i = 0; i < g->n; i++)
    {
        cout << sensors[i].sensor_id << ": ";
        AdjNode* curr = g->adj[i];
        while (curr)
        {
            cout << "(" << sensors[curr->dest].sensor_id << ", w=" << curr->weight << ") ";
            curr = curr->next;
        }
        cout << endl;
    }
}

// Remove Sensor function, remap the indexes of the sensors 

void remove_sensor(Graph*& g, Sensor*& sensors, int**& matrix, int id)
{
    if (id < 0 || id >= g->n) {
        cout << "Invalid sensor ID!" << endl;
        return;
    }

    int old_n = g->n;
    int new_n = old_n - 1;
    if (new_n == 0) {
        cout << "Cannot remove the last sensor!" << endl;
        return;
    }

    // Remove outgoing edges
    AdjNode* curr = g->adj[id];
    while (curr) {
        AdjNode* del = curr;
        curr = curr->next;
        delete del;
    }

    // Remove incoming edges
    for (int i = 0; i < old_n; i++)
    {
        if (i == id) continue;
        AdjNode* curr = g->adj[i];
        AdjNode* prev = nullptr;
        while (curr)
        {
            if (curr->dest == id)
            {
                if (prev) prev->next = curr->next;
                else g->adj[i] = curr->next;
                AdjNode* del = curr;
                curr = curr->next;
                delete del;
            }
            else
            {
                if (curr->dest > id) curr->dest--;
                prev = curr;
                curr = curr->next;
            }
        }
    }

    // Shift adjacency array
    for (int i = id; i < new_n; i++)
        g->adj[i] = g->adj[i + 1];
    g->adj[new_n] = nullptr;
    g->n = new_n;

    // Update sensors with consecutive IDs
    Sensor* new_sensors = new Sensor[new_n];
    for (int i = 0, j = 0; i < old_n; i++)
    {
        if (i == id) continue;
        new_sensors[j].sensor_id = j; // assign consecutive IDs
        new_sensors[j].sensor_speed = sensors[i].sensor_speed;
        j++;
    }
    delete[] sensors;
    sensors = new_sensors;

    // Update connectivity matrix
    int** new_matrix = new int* [new_n];
    for (int i = 0; i < new_n; i++)
        new_matrix[i] = new int[new_n];

    for (int i = 0, new_i = 0; i < old_n; i++)
    {
        if (i == id) continue;
        for (int j = 0, new_j = 0; j < old_n; j++)
        {
            if (j == id) continue;
            new_matrix[new_i][new_j++] = matrix[i][j];
        }
        new_i++;
    }

    for (int i = 0; i < old_n; i++)
        delete[] matrix[i];
    delete[] matrix;
    matrix = new_matrix;

    cout << "Sensor " << id << " removed successfully!" << endl;
}

// Cleanup all memory 

void free_all(Graph*& g, Sensor*& sensors, int**& matrix)
{
    if (g)
    {
        int n = g->n; // store before deleting g
        for (int i = 0; i < n; i++)
        {
            AdjNode* curr = g->adj[i];
            while (curr)
            {
                AdjNode* tmp = curr;
                curr = curr->next;
                delete tmp;
            }
        }
        delete[] g->adj;
        delete g;
        g = nullptr;

        // Free connectivity matrix
        if (matrix)
        {
            for (int i = 0; i < n; i++)
                delete[] matrix[i];
            delete[] matrix;
            matrix = nullptr;
        }
    }

    if (sensors)
    {
        delete[] sensors;
        sensors = nullptr;
    }
}

// Custom Queue implementation using linked list

struct Queue
{
    int* data;
    int front;
    int rear;
    int size;
    int capacity;
};

Queue* create_queue(int capacity)
{
    Queue* q = new Queue;
    q->capacity = capacity;
    q->data = new int[capacity];
    q->front = 0;
    q->rear = -1;
    q->size = 0;
    return q;
}

void enqueue(Queue* q, int value)
{
    if (q->size == q->capacity) return;
    q->rear = (q->rear + 1) % q->capacity;
    q->data[q->rear] = value;
    q->size++;
}

int dequeue(Queue* q)
{
    if (q->size <= 0) return -1;
    int value = q->data[q->front];
    q->front = (q->front + 1) % q->capacity;
    q->size--;
    return value;
}

void free_queue(Queue* q)
{
    delete[] q->data;
    delete q;
}

// BFS implementation using Queue

void bfs(Graph* g, int start_id)
{
    if (!g || start_id < 0 || start_id >= g->n)
    {
        cout << "Invalid sensor ID!" << endl;
        return;
    }

    bool* visited = new bool[g->n]{ false };
    Queue* q = create_queue(g->n);

    visited[start_id] = true;
    enqueue(q, start_id);

    cout << "BFS traversal starting from sensor " << start_id << ": ";

    while (q->size > 0)
    {
        int u = dequeue(q);
        cout << u << " ";

        // Store unvisited neighbors
        int count = 0;
        AdjNode* curr = g->adj[u];
        while (curr) { if (!visited[curr->dest]) count++; curr = curr->next; }

        int* neighbors = new int[count];
        curr = g->adj[u];
        int idx = 0;
        while (curr) { if (!visited[curr->dest]) neighbors[idx++] = curr->dest; curr = curr->next; }

        // Sort neighbors
        for (int i = 0; i < count - 1; i++)
            for (int j = 0; j < count - i - 1; j++)
                if (neighbors[j] > neighbors[j + 1])
                {
                    int tmp = neighbors[j];
                    neighbors[j] = neighbors[j + 1];
                    neighbors[j + 1] = tmp;
                }

        for (int i = 0; i < count; i++)
        {
            visited[neighbors[i]] = true;
            enqueue(q, neighbors[i]);
        }

        delete[] neighbors;
    }

    cout << endl;
    delete[] visited;
    free_queue(q);
}

// Custom stack implementation using linked list

struct StackNode
{
    int value;
    StackNode* next;
};

struct Stack
{
    StackNode* top;
};

Stack* create_stack()
{
    Stack* s = new Stack;
    s->top = nullptr;
    return s;
}

bool is_stack_empty(Stack* s)
{
    return s->top == nullptr;
}

void push(Stack* s, int value)
{
    StackNode* node = new StackNode;
    node->value = value;
    node->next = s->top;
    s->top = node;
}

int pop(Stack* s)
{
    if (is_stack_empty(s))
        return -1;

    StackNode* node = s->top;
    int value = node->value;
    s->top = node->next;
    delete node;
    return value;
}

void free_stack(Stack* s)
{
    while (!is_stack_empty(s))
        pop(s);
    delete s;
}

// DFS using linked list stack 

void dfs(Graph* g, int start_id)
{
    if (!g || start_id < 0 || start_id >= g->n)
    {
        cout << "Invalid sensor ID!" << endl;
        return;
    }

    bool* visited = new bool[g->n]{ false };

    Stack* s = create_stack();
    push(s, start_id);

    cout << "DFS traversal starting from sensor " << start_id << ": ";

    while (!is_stack_empty(s))
    {
        int u = pop(s);

        if (visited[u])
            continue;

        visited[u] = true;
        cout << u << " ";

        // Count unvisited neighbors
        int count = 0;
        AdjNode* curr = g->adj[u];
        while (curr)
        {
            if (!visited[curr->dest])
                count++;
            curr = curr->next;
        }

        // Store neighbors
        int* neighbors = new int[count];
        curr = g->adj[u];
        int idx = 0;
        while (curr)
        {
            if (!visited[curr->dest])
                neighbors[idx++] = curr->dest;
            curr = curr->next;
        }

        // Sort neighbors in descending order for stack
        for (int i = 0; i < count - 1; i++)
            for (int j = 0; j < count - i - 1; j++)
                if (neighbors[j] < neighbors[j + 1])
                {
                    int tmp = neighbors[j];
                    neighbors[j] = neighbors[j + 1];
                    neighbors[j + 1] = tmp;
                }

        // Push neighbors onto stack
        for (int i = 0; i < count; i++)
            push(s, neighbors[i]);

        delete[] neighbors;
    }

    cout << endl;

    delete[] visited;
    free_stack(s);
}

// Djikstra implementation

void dijkstra(Graph* g, Sensor* sensors, int start_id, int end_id)
{
    if (!g || start_id < 0 || start_id >= g->n || end_id < 0 || end_id >= g->n)
    {
        cout << "Invalid sensor IDs!" << endl;
        return;
    }

    // Initialize distances and predecessors
    double* dist = new double[g->n];
    int* prev = new int[g->n];
    bool* visited = new bool[g->n];

    for (int i = 0; i < g->n; i++)
    {
        dist[i] = 1e9; // "infinity"
        prev[i] = -1;
        visited[i] = false;
    }

    dist[start_id] = 0;

    for (int count = 0; count < g->n; count++)
    {
        // Find the unvisited node with the smallest distance
        int u = -1;
        double min_dist = 1e9;
        for (int i = 0; i < g->n; i++)
        {
            if (!visited[i] && dist[i] < min_dist)
            {
                min_dist = dist[i];
                u = i;
            }
        }

        if (u == -1) break; // no reachable node left
        visited[u] = true;

        // Relax edges
        AdjNode* curr = g->adj[u];
        while (curr)
        {
            int v = curr->dest;
            double w = curr->weight;
            if (!visited[v] && dist[u] + w < dist[v])
            {
                dist[v] = dist[u] + w;
                prev[v] = u;
            }
            curr = curr->next;
        }
    }

    // Check if end node is reachable
    if (dist[end_id] == 1e9)
    {
        cout << "No path exists between sensor " << start_id
            << " and sensor " << end_id << "!" << endl;
    }
    else
    {
        // Reconstruct path using stack
        Stack* path = create_stack();
        for (int at = end_id; at != -1; at = prev[at])
            push(path, at);

        cout << "Shortest path from sensor " << start_id
            << " to sensor " << end_id << ": ";
        while (!is_stack_empty(path))
        {
            int node = pop(path);
            cout << node;
            if (!is_stack_empty(path))
                cout << " -> ";
        }
        cout << "\nTotal time: " << dist[end_id] << endl;
        free_stack(path);
    }

    delete[] dist;
    delete[] prev;
    delete[] visited;
}

// Calculating eccentricity and center node

void eccentricity_and_center(Graph* g)
{
    const double INF = 1e9;

    int n = g->n;
    double* dist = new double[n];
    bool* visited = new bool[n];

    double min_ecc = INF;
    int center = -1;

    cout << "\nEccentricities:\n";

    for (int src = 0; src < n; src++)
    {
        // Dijkstra initialization
        for (int i = 0; i < n; i++)
        {
            dist[i] = INF;
            visited[i] = false;
        }

        dist[src] = 0.0;

        // Dijkstra (array based)
        for (int count = 0; count < n - 1; count++)
        {
            double min_dist = INF;
            int u = -1;

            for (int i = 0; i < n; i++)
            {
                if (!visited[i] && dist[i] < min_dist)
                {
                    min_dist = dist[i];
                    u = i;
                }
            }

            if (u == -1)
                break;

            visited[u] = true;

            AdjNode* curr = g->adj[u];
            while (curr)
            {
                int v = curr->dest;
                double w = curr->weight;

                if (!visited[v] && dist[u] + w < dist[v])
                    dist[v] = dist[u] + w;

                curr = curr->next;
            }
        }

        // Compute eccentricity
        double ecc = 0.0;
        bool disconnected = false;

        for (int i = 0; i < n; i++)
        {
            if (dist[i] == INF)
            {
                disconnected = true;
                break;
            }
            if (dist[i] > ecc)
                ecc = dist[i];
        }

        if (disconnected)
        {
            cout << "Sensor " << src << ": INF (not all sensors reachable)\n";
        }
        else
        {
            cout << "Sensor " << src << ": " << ecc << "\n";

            if (ecc < min_ecc)
            {
                min_ecc = ecc;
                center = src;
            }
        }
    }

    // Print central sensor 
    if (center == -1)
    {
        cout << "\nNo central sensor exists (graph is disconnected).\n";
    }
    else
    {
        cout << "\nCentral sensor: " << center
            << "\nEccentricity: " << min_ecc << "\n";
    }

    delete[] dist;
    delete[] visited;
}


// main function to run the code 
int main()
{
    int n;
    cout << "Please enter the number of nodes n: ";
    cin >> n;

    double p;
    double min_p = 1.0 / n;
    double max_p = log((double)n) / n;

    do
    {
        cout << "Please enter p in range [" << min_p << ", " << max_p << "]: ";
        cin >> p;
    } while (p < min_p || p > max_p);

    unsigned long long seed;
    cout << "Please enter the seed value: ";
    cin >> seed;

    unsigned long long n_bbs = 11 * 19;
    seed %= n_bbs;
    if (seed == 0) seed = 1;

    // Initialize sensors
    Sensor* sensors = new Sensor[n];
    for (int i = 0; i < n; i++)
    {
        sensors[i].sensor_id = i;
        sensors[i].sensor_speed = random_int_range(seed, n_bbs, 1, 100);
    }

    Graph* g = create_graph(n);
    int** matrix = create_matrix(n);

    generate_random_graph(g, sensors, matrix, p, seed, n_bbs);

    int choice;
    do
    {
        cout << "\n1. Remove sensor"
            << "\n2. Print graph"
            << "\n3. Print connectivity matrix"
            << "\n4. BFS traversal"
            << "\n5. DFS traversal"
            << "\n6. Dijkstra shortest path"
            << "\n7. Eccentricity & central sensor"
            << "\n0. Exit\nChoice: ";

        cin >> choice;

        if (choice == 1)
        {
            int id;
            cout << "Enter sensor id to remove: ";
            cin >> id;
            remove_sensor(g, sensors, matrix, id);
        }
        else if (choice == 2)
        {
            print_graph(g, sensors);
        }
        else if (choice == 3)
        {
            print_matrix(matrix, g->n);
        }
        else if (choice == 4)
        {
            int start_id;
            cout << "Enter starting sensor id: ";
            cin >> start_id;
            bfs(g, start_id);
        }
        else if (choice == 5)
        {
            int start_id;
            cout << "Enter starting sensor id: ";
            cin >> start_id;
            dfs(g, start_id);
        }
        else if (choice == 6)
        {
            int start_id, end_id;
            cout << "Enter starting sensor id: ";
            cin >> start_id;
            cout << "Enter ending sensor id: ";
            cin >> end_id;
            dijkstra(g, sensors, start_id, end_id);
        }
        else if (choice == 7)
        {
            eccentricity_and_center(g);
        }

    } while (choice != 0);

    free_all(g, sensors, matrix);
    return 0;
}
