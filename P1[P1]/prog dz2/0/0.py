def data(file_name):
    flights = []
    try:
        with open(file_name, 'r', encoding='utf-8') as f:
            for line in f:
                line = line.strip()
                if line:
                    airline,route,schedule_str = line.split("|")
                    dep_city, arr_city = route.split("->")
                    schedule_parts = schedule_str.split(";")
                    schedule = []
                    for part in schedule_parts:
                        time_range, price = part.split(",")
                        dep_time,arr_time = time_range.split("-")
                        schedule.append([dep_time,arr_time, int(price)])
                    flights.append([airline,dep_city,arr_city,schedule])
        return flights
    except FileNotFoundError:
        print("DAT_GRESKA", end='')
        return None
    
def flights_out(flights):
    with open("flights_out.txt", 'w', encoding='utf-8') as f:
        for airline,dep_city,arr_city,schedule in flights:
            f.write(f"{airline} | {dep_city}->{arr_city}\n")
            for dep_time, arr_time, price in schedule:
                f.write(f"\t{dep_time}-{arr_time} | {price}\n")


def flights_direct(flights):
    routes = []
    for airline, dep_city, arr_city, schedule in flights:
        route = dep_city + "->" + arr_city
        found = False
        for i in routes:
            if i[0] == route:
                i[1].append([airline, schedule])
                found = True
                break
        if not found:
            routes.append([route,[[airline,schedule]]])

    routes.sort(key=lambda x: x[0])

    with open ("flights_direct.txt", 'w', encoding='utf-8') as f:
        for route, company in routes:
            f.write(f"{route}\n")
            company.sort(key=lambda x: x[0])
            for airline, schedule in company:
                f.write(f"\t{airline}\n")
                for dep_time, arr_time, price in schedule:
                    f.write(f"\t\t{dep_time}-{arr_time} | {price}\n")

def flights_indirect(flights,dep_city,arr_city):
    routes = []
    for flight1 in flights:
        if flight1[1] == dep_city:
            mid_city = flight1[2]
            for flight2 in flights:
                if flight2[1] == mid_city and flight2[2] == arr_city:
                    routes.append((dep_city,mid_city,arr_city))
    
    routes = sorted(list(set(routes)))

    with open("flights_indirect.txt", "w", encoding="utf-8") as f:
        for city1,city2,city3 in routes:
            f.write(f"{city1}->{city2}->{city3}\n")
            first_legs = []
            second_legs = []

            for flight1 in flights:
                if flight1[1] == city1 and flight1[2] == city2:
                    for departure_time, arrival_time, price in flight1[3]:
                        first_legs.append((departure_time,arrival_time,price, flight1[0]))
                if flight1[1] == city2 and flight1[2] == city3:
                    for departure_time,arrival_time,price in flight1[3]:
                        second_legs.append((departure_time,arrival_time,price, flight1[0]))

            first_legs.sort(key=lambda x: (x[0], (int(x[1][:2])*60 + int(x[1][3:])) - (int(x[0][:2])*60 + int(x[0][3:])), x[3]))
            second_legs.sort(key=lambda x: (x[0], (int(x[1][:2])*60 + int(x[1][3:])) - (int(x[0][:2])*60 + int(x[0][3:])), x[3]))

            unique_first = []
            for leg in first_legs:
                if leg not in unique_first:
                    unique_first.append(leg)

            unique_second = []
            for leg in second_legs:
                if leg not in unique_second:
                    unique_second.append(leg)

            for fl1 in unique_first:
                arr1_hour, arr1_min = map(int, fl1[1].split(":"))
                arr1_minutes = arr1_hour*60 + arr1_min
                write_second_legs = []
                for fl2 in unique_second:
                    dep2_hour, dep2_min = map(int, fl2[0].split(":"))
                    dep2_minutes = dep2_hour*60 + dep2_min
                    if dep2_minutes>= arr1_minutes:
                        write_second_legs.append(fl2)
                if write_second_legs:
                    f.write(f"\t{fl1[0]}-{fl1[1]} | {fl1[3]} | {fl1[2]}\n")
                    for fl2 in write_second_legs:
                        f.write(f"\t\t{fl2[0]}-{fl2[1]} | {fl2[3]} | {fl2[2]}\n")


file_data = data("flights.txt")
if file_data:
    write_flights_out = flights_out(file_data)
    write_flights_direct = flights_direct(file_data)
    user_route = input().strip()
    dep_city, arr_city = user_route.split("->")
    write_flights_indirect = flights_indirect(file_data, dep_city, arr_city)