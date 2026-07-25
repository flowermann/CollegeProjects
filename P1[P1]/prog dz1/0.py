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

def dohvati_spisak_proizvoda(duzina_spiska_proizvoda):
    try:
        spisak_proizvoda = []
        for i in range(duzina_spiska_proizvoda):
            proizvod = input()
            spisak_proizvoda.append(proizvod)
    except:
        return
    return spisak_proizvoda

def dohvati_cene_proizvoda(duzina_spiska_cena):
    try:
        spisak_cena_proizvoda = []
        for i in range(duzina_spiska_cena):
            informacije_proizvoda = input().split(",")
            if len(informacije_proizvoda) != 4:
                return None  
            naziv_proizvoda = informacije_proizvoda[0]
            naziv_prodavnice = informacije_proizvoda[1]
            cena_proizvoda = float(informacije_proizvoda[2])
            popust = int(informacije_proizvoda[3])
            spisak_cena_proizvoda.append([naziv_proizvoda, naziv_prodavnice, cena_proizvoda, popust])
        return spisak_cena_proizvoda
    except:
        return None


def izracunaj_popust(spisak_info_proizvoda):
    novi_spisak_sa_popustima = []
    for info in spisak_info_proizvoda:
        naziv_proizvoda = info[0]
        prodavnica = info[1]
        cena = info[2]
        popust_proc = info[3]
        cena_sa_popustom = cena * (1 - popust_proc / 100)
        usteda = cena - cena_sa_popustom
        novi_spisak_sa_popustima.append([naziv_proizvoda, prodavnica, cena, usteda, cena_sa_popustom])
    return novi_spisak_sa_popustima


def kreiraj_plan_kupovine(spisak_proizvoda, spisak_proizvoda_sa_popustima):
    plan_kupovine = []
    for zeljeni_proizvod in spisak_proizvoda:
        najniza_cena = float('inf')
        najbolja_ponuda = None
        for i in range(len(spisak_proizvoda_sa_popustima)):
            proizvod_sa_popustom = spisak_proizvoda_sa_popustima[i]
            if zeljeni_proizvod == proizvod_sa_popustom[0]:
                if proizvod_sa_popustom[-1] < najniza_cena:
                    najniza_cena = proizvod_sa_popustom[-1]
                    najbolja_ponuda = proizvod_sa_popustom
        if najbolja_ponuda is not None:
            plan_kupovine.append(najbolja_ponuda)
        else:
            plan_kupovine.append([zeljeni_proizvod, "X", 0.0, 0.0, "X"])
    return plan_kupovine


def ispis(plan_kupovine):
    usteda = 0
    for proizvod in plan_kupovine:
        if proizvod[-1] == "X":
            print(f"{proizvod[0]} - X")
        else:
            print(f"{proizvod[0]} - {proizvod[-1]:.2f} ({proizvod[1]})")
            usteda += proizvod[3]
    print(f"Usteda: {usteda:.2f}")

duzine = dohvati_duzine()
if duzine is None:
    pass
else:
    spisak_proizvoda = dohvati_spisak_proizvoda(duzine[0])
    if spisak_proizvoda is None:
        pass
    else:
        spisak_info_proizvoda = dohvati_cene_proizvoda(duzine[1])
        if spisak_info_proizvoda is None:
            pass
        else:
            spisak_proizvoda_sa_popustima = izracunaj_popust(spisak_info_proizvoda)
            plan_kupovine = kreiraj_plan_kupovine(spisak_proizvoda, spisak_proizvoda_sa_popustima)
            ispis(plan_kupovine)