# Prova Finale Ingegneria del Software - Anno Accademico 2019/20
Il progetto consiste nello sviluppo di una versione software del gioco da tavolo **Santorini**.
## Documentazione
All'interno della cartella relativa alle [final deliveries](/deliveries/final/) sono presenti:
 - [Coverage](/deliveries/final/FinalCoverageReport) report finale della coverage
 - [Jar](/deliveries/final/Jar) file .jar per Windows10 e MacOS
 - [JavaDoc](/deliveries/final/JavaDoc) documentazione dettagliata relativa a tutte le classi del progetto
 - [ModelUML](/deliveries/final/UML/ModelUML) schema di alto livello relativo al model
 - [DetailedUML](/deliveries/final/UML/DetailedUML) schema dettagliato relativo a tutti i package del progetto con relative classi
## Funzionalità Sviluppate
| Funzionalità | Stato |
|:-----------------------|:------------------------------------:|
| Regole Semplificate | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Regole Complete | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistenza | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Divinità Avanzate | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
## Jar
Per avviare uno qualsiasi degli applicativi è necessario avere installato sul proprio dispositivo Java 14 o superiore (anche il compiler deve soddisfare questo requisito minimo).

Per verificarlo  è sufficiente digitare nel terminale i seguenti comandi:

`java -version`

`javac -version`

Si consiglia, per questioni di compatibilità di avviare solo i Jar relativi al proprio sistema operativo, per evitare comportamenti anomali.

### Server
Nella stessa cartella dove sarà inserito il Jar del **Server** sarà necessario creare una cartella chiamata "*temp*" con al suo interno un file "*savedGame.txt*" contenente
come prima riga "**-1**".

Le cartelle [Windows10](/deliveries/final/Jar/windows) e [MacOS](/deliveries/final/Jar/mac) sono già state configurate correttamente, quindi è possibile estrarle senza 
attuare configurazioni.

**IMPORTANTE**

Sarà necessario avviare l'applicativo da terminale con l'accortezza di posizionarsi allo stesso livello della cartella dove è presente il Jar, per permettere il 
corretto funzionamento delle funzionalità di salvataggio/caricamento (Persistenza).

Per l'avvio digitare il seguente comando:

`java -jar Server.jar`

### CLI
Non riconoscendo **Windows** parte dei caratteri utilizzati è consigliato l'utilizzo della **Linux Bash Shell**, scaricabile dall'AppStore sotto il nome di **Ubuntu**; 
per scaricarla è necessario abilitare il *SottoAmbiente Linux* per Windows seguendo la seguente [guida](https://www.howtogeek.com/249966/how-to-install-and-use-the-linux-bash-shell-on-windows-10/) e successivamente installare Java 14 per **Linux**.

Per l'avvio digitare il comando:

`java -jar ClientCli.jar`
### GUI
Per l'avvio digitare il seguente comando:

`java -jar ClientGui.jar`

## Componenti
**Gruppo PSP49**
 - [Andrea Manocchio](https://github.com/andremanoc)
 - [Andrea Menta](https://github.com/Menta99)
 - [Giovanni Nisti](https://github.com/GiovanniN98)
