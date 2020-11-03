//   Concepeti un program care:
//1.✓Citeste elementele unui automat finit. Descrieti, folosind BNF sau EBNF, cum poate sa arate continutul fisierului.
//2.✓Afiseaza, prin intermediul unui meniu: multimea starilor, alfabetul, tranzitiile, multimea starilor finale.
//3.✓Pentru un automat finit determinist, verifica daca o secventa este acceptata de automatul finit.
//4.✓Pentru un automat finit determinist, determina cel mai lung prefix dintr-o secventa data, care este o secventa acceptata de automat.

//5.✓Creati un fisier care contine descrierea unui AFD ce descrie constantelor intregi (literali) din C/C++.
//   Folositi-l ca data de test pentru programul dumneavoastra.

import ui.GUI;

import java.io.IOException;

//https://i.imgur.com/mkahLjq.png - integer
//https://i.imgur.com/OfoBzlq.png - integer with everything
//https://i.imgur.com/8UXbKer.png - integer with everything, but follows C++ to spec
//https://i.imgur.com/atBstWY.png - double, does not accept [number].0 ~ (0|[1-9][0-9]*)(\.[0-9]*[1-9])?
//https://i.imgur.com/aU3wQWW.png - double, accepts [number].0 ~ (0|[1-9][0-9]*)(\.([0-9]*[1-9]|0))?

public class Main {
    public static void main(String[] args) throws IOException {
        new GUI().run(args);
    }
}
