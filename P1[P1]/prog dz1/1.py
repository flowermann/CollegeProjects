# 1) OBRADA ULAZA 
def vezbe():
    '''Funkcija koja hvata vezbe sa ulaza'''
    try:
        n = int(input())
        if n <=0:
            return 
        lista_vezbi= []
        for i in range(n):
            vezba = input().split(',')
            vezba[2] = int(vezba[2])
            vezba[3] = int(vezba[3])
            lista_vezbi.append(vezba)
    except:
        return
    return lista_vezbi

def zelje():
    '''Funkcija koja hvata zelje sa ulaza'''
    try:
        zelja = input().split()
        zelja[0]=int(zelja[0])
    except:
        return
    return zelja

# 2) FILTRIRANJE

def filter_misici(vezbe, zelje):
    '''Funkcija koja zadrzava vezbe sa zadatim misicnim grupama'''
    filtrirani_misici = []
    ciljani_misic = zelje[1]
    # iteriramo kroz listu vezbi i ostavljamo samo primer koji se poklapa sa ciljanim_misicem
    for vezba in vezbe:
        if vezba[1] == ciljani_misic:
            filtrirani_misici.append(vezba)
    return filtrirani_misici

def dodaj_intenzitet(vezbe):
    '''Funkcija koja dodaje intenzitet svakoj vezbi na osnovu kalorija'''
    vezbe_sa_intenzitetom = vezbe.copy()
    for vezba in vezbe_sa_intenzitetom:
        if vezba[3]<60:
            vezba.append("slab")
        if 60<=vezba[3]<100:
            vezba.append("srednji")
        if vezba[3]>=100:
            vezba.append("jak")
    return vezbe_sa_intenzitetom

def filter_intenzitet(vezbe, zelje):
    '''Funkcija koja zadrzava vezbe zadatog intenziteta'''
    filtrirani_intenzitet = []
    ciljani_intenzitet = zelje[2]
    # iteriramo kroz listu vezbi i ostavljamo samo primer koji se poklapa sa ciljanim_intenzitetom
    for vezba in vezbe:
        if vezba[4] == ciljani_intenzitet:
            filtrirani_intenzitet.append(vezba)
    return filtrirani_intenzitet

# 3) Kreiranje plana treninga
def plan_treninga(filtrirane_vezbe, zelje):
    '''Finalni plan sa nazivom vezbe, trajanjem u minutima i kalorijama'''
    konacna_lista_vezbi = []
    ukupan_broj_minuta = 0
    ukupan_broj_kalorija = 0

    for vezba in filtrirane_vezbe:
        trajanje = vezba[2]
        kalorije_na_10min = vezba[3]

        if ukupan_broj_minuta + trajanje <= zelje[0]:
            kalorije = (trajanje / 10) * kalorije_na_10min
            vezba.append(kalorije)
            ukupan_broj_minuta += trajanje
            ukupan_broj_kalorija += kalorije
            konacna_lista_vezbi.append(vezba)
        else:
            break

    return konacna_lista_vezbi, ukupan_broj_minuta, ukupan_broj_kalorija

# 4) ispis
def ispis(konacna_lista_vezbi, ukupan_broj_minuta, ukupan_broj_kalorija):
    if not konacna_lista_vezbi:
        print("Nema vežbi po zadatom kriterijumu.")
    for vezba in konacna_lista_vezbi:
        print(f"{vezba[0]} - {vezba[2]} min - {vezba[-1]:.2f} kcal")
    print(f"Ukupno trajanje: {ukupan_broj_minuta} min")
    print(f"Ukupno kalorija: {ukupan_broj_kalorija:.2f}",end=' kcal')

# input
vezbe = vezbe()
if vezbe is None:
    pass
else:
    zelje = zelje()
    if zelje is None:
        pass
    else:
        # filtriranje na osnovu misica
        vezbe_sa_zeljenim_misicima = filter_misici(vezbe,zelje)
        # dodajemo promenljivu intenzitet u svaku listu
        vezbe_sa_intenzitetom = dodaj_intenzitet(vezbe_sa_zeljenim_misicima)
        # filtriramo na osnovu intenziteta
        filtrirane_vezbe = filter_intenzitet(vezbe_sa_intenzitetom, zelje)
        # kreiramo konacnu listu vezbi, racunamo ukupan broj minuta i kalorija
        konacna_lista_vezbi, ukupan_broj_minuta, ukupan_broj_kalorija = plan_treninga(filtrirane_vezbe,zelje)
        # ispisujemo sve podatke 
        ispis(konacna_lista_vezbi, ukupan_broj_minuta, ukupan_broj_kalorija)