def read_data(file_name):
    flights = []
    try:
        with open(file_name, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if not line:
                    continue
                airline, route, schedule_str = line.split("|")
                dep_city, arr_city = route.split("->")
                schedule_parts = schedule_str.split(";")
                schedule = []
                for part in schedule_parts:
                    time_range, price = part.split(",")
                    dep_time, arr_time = time_range.split("-")
                    schedule.append([dep_time, arr_time, int(price)])
                flights.append([airline, dep_city, arr_city, schedule])
        return flights
    except FileNotFoundError:
        print("DAT_GRESKA")
        return None


def flights_out(flights):
    with open("flights_out.txt", "w", encoding="utf-8") as f:
        for airline, dep_city, arr_city, schedule in flights:
            f.write(f"{airline} | {dep_city}->{arr_city}\n")
            for dep_time, arr_time, price in schedule:
                f.write(f"\t{dep_time}-{arr_time} | {price}\n")


def flights_direct(flights):
    # sortiranje po ruti pa po kompaniji
    routes = []
    for airline, dep_city, arr_city, schedule in flights:
        route = dep_city + "->" + arr_city
        found = False
        for r in routes:
            if r[0] == route:
                r[1].append([airline, schedule])
                found = True
                break
        if not found:
            routes.append([route, [[airline, schedule]]])

    routes.sort(key=lambda x: x[0])

    with open("flights_direct.txt", "w", encoding="utf-8") as f:
        for route, comps in routes:
            f.write(f"{route}\n")
            comps.sort(key=lambda x: x[0])
            for airline, schedule in comps:
                f.write(f"\t{airline}\n")
                for dep_time, arr_time, price in schedule:
                    f.write(f"\t\t{dep_time}-{arr_time} | {price}\n")


def flights_indirect(data, dep_city, arr_city):
    routes = []
    for flight in data:
        airline, dep, arr, flights_list = flight
        if dep == dep_city:
            for f in data:
                if f[1] == arr and f[2] == arr_city:
                    routes.append((dep, arr, arr_city))
    routes = sorted(list(set(routes)))

    with open("flights_indirect.txt", "w", encoding="utf-8") as f:
        for route in routes:
            city1, city2, city3 = route
            f.write(f"{city1}->{city2}->{city3}\n")
            first_legs = []
            second_legs = []
            for fl in data:
                if fl[1] == city1 and fl[2] == city2:
                    for dep_t, arr_t, price in fl[3]:
                        first_legs.append((dep_t, arr_t, price, fl[0]))
                if fl[1] == city2 and fl[2] == city3:
                    for dep_t, arr_t, price in fl[3]:
                        second_legs.append((dep_t, arr_t, price, fl[0]))

            # sortiranje po pravilima
            first_legs.sort(key=lambda x: (x[0], 
                                           (int(x[1][:2])*60+int(x[1][3:])) - (int(x[0][:2])*60+int(x[0][3:])), 
                                           x[3]))
            second_legs.sort(key=lambda x: (x[0], 
                                            (int(x[1][:2])*60+int(x[1][3:])) - (int(x[0][:2])*60+int(x[0][3:])), 
                                            x[3]))

            # izbacivanje duplikata
            first_legs_unique = []
            for leg in first_legs:
                if leg not in first_legs_unique:
                    first_legs_unique.append(leg)

            second_legs_unique = []
            for leg in second_legs:
                if leg not in second_legs_unique:
                    second_legs_unique.append(leg)

            # upis
            for fl1 in first_legs_unique:
                f.write(f"\t{fl1[0]}-{fl1[1]} | {fl1[3]} | {fl1[2]}\n")
                for fl2 in second_legs_unique:
                    f.write(f"\t\t{fl2[0]}-{fl2[1]} | {fl2[3]} | {fl2[2]}\n")


# Glavni deo
flights = read_data("flights.txt")
if flights:
    flights_out(flights)
    flights_direct(flights)
    user_route = input().strip()
    dep_city, arr_city = user_route.split("->")
    flights_indirect(flights, dep_city, arr_city)
