def dohvati_duzine():
    try:
        unos = input().split()
        if len(unos) != 2:
            return None
        duzine = []
        for t in unos:
            broj = int(t)
            if broj < 0:
                return None
            duzine.append(broj)
        return duzine
    except:
        return None
duzine = dohvati_duzine()
print(duzine)