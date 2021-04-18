# Plastic Consumption

### Descrizione

L'Applicazione si occupa di censire settimanalmente il consumo di plastica da parte degli utenti censiti. 
In questa demo vengono creati 4 utenti
  - Pagot
  - fio
  - curtis
  - gina

Tutti questi utenti hanno la seguente password: *password*.
Per tutti questi utenti vengono creati dati causuali dal 2000 fino alla data attuale.

### Installazione

1. Clonare il progetto
```
git clone https://github.com/rhllor/PlasticConsumption.git
```

2. Accedere alla cartella appena scaricata
```
cd PlasticConsumption
```

3. Compilare l'applicazione
```
.\mvnw install
```

4. Crare l'immagine docker.
```
docker build service --tag plasticconsumptionservice
```

5. Creare ed eseguire un container dall'immagine precedentemente generata
```
docker run -d -p 8080:8080 plasticconsumptionservice
```

6. Una volta in esecuzione il container si accede alla documentazione del servizio al seguente indirizzo: http://localhost:8080/swagger-ui.html