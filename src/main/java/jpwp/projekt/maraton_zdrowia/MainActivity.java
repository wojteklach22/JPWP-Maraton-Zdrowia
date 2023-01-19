package jpwp.projekt.maraton_zdrowia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;



/**
 * Klasa główna programu, od razu po włączeniu aplikacji pojawia się okno gry
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Przycisk w grze odpowiadajacy za przemieszczenie gracza na bieżnię powyżej
     */
    private Button bUp;
    /**
     * Przycisk w grze odpowiadajacy za przemieszczenie gracza na bieżnię poniżej
     */
    private Button bDown;
    /**
     * Przycisk startujący grę
     */
    private Button bStart;
    /**
     * Obiekt przechowujący grafikę oraz położenie gracza na planszy
     */
    private ImageView gracz;
    /**
     * Oddzielenie torów bieżni
     */
    private ImageView drawLinia, drawLinia2, drawLinia3;
    /**
     * Obiekt przechowujący grafikę serca
     */
    private ImageView[] serca = new ImageView[3];
    /**
     * Obiekt pokazujący liczbę zgromadzonych punktów na ekranie
     */
    private TextView Tpunkty;
    /**
     * Obiekt pokazujący aktualny poziom gracza na ekranie
     */
    private TextView Tpoziom;

    /**
     * Zmienna przechowująca punkty gracza
     */
    private int Ipunkty =0;
    /**
     * Zmienna przechowująca liczbę żyć gracza
     */
    private int Izycia = 3;
    /**
     * Zmienna przechowująca poziom na którym gracz się znajduje
     */
    private int Ipoziom=1;

    /**
     * Zmienna odpowiedzialna za czas gry
     */
    private long time = 0;
    /**
     * Licznik czasu gry
     */
    private CountDownTimer countDownTimer;
    /**
     * Obiekt wyświetlający czas pozostały na ekranie
     */
    public TextView timer_txt;
    /**
     * Licznik czasu pozostalego do końca poziomu
     */
    private long timeLeft;
    /**
     * Zmienna sprawdzająca czy czas został uruchomiony
     */
    private boolean isTimerRun;
    /**
     * Layout, na którym gra została stworzona
     */
    public LinearLayout linearLayout;
    /**
     * Obiekt odpowiadający za losowanie, głównie zdjęcia jedzenia, które ma się pokazać
     */
    Random random = new Random();
    /**
     * Tablica przechowująca informacje o położeniu, na płaszczyźnie OXY, danego toru(bieżni)
     */
    float[] bieznie_wys = {1175, 1320, 1465};
    /**
     * Tablica korygująca położenie toru, gdy obiekt już istnieje na planszy
     */
    float[] pozycjonowanie = {5,145, 290};
    /**
     * Liczba elementów na ekranie (liczona od 0) - =2 => 3 elementy
     */
    int elementow_ekran = 2;
    /**
     * Tablica przechowująca grafiki zdrowych obrazków
     */
    int[] zdrowe_obrazki;
    /**
     * Tablica przechowująca grafiki niezdrowych obrazków
     */
    int[] niezdrowe_obrazki;
    /**
     * Wywołanie klasy odpowiadającej za wyświetlanie grafiki nadlatującego jedzenia na ekranie
     * Maksymalna ilość obiektów na planszy ustawiono na 100 (szacowana ilość maksymalna = 50)
     */
    Jedzenie[] obiekt = new Jedzenie[100];
    /**
     * zmienna odpowiadająca za sprawdzenie możliwości wygenerowanie nowego elemntu co określony interwał czasu
     * Prewencyjnie, aby tworzony był tylko jeden element na daną sekundę
     */
    boolean nowy = true;
    /**
     * Zmienna służąca do ustalenia czy może zostać wygenerowany nowy element na planszy
     * Prewencyjnie, aby tworzony był tylko jeden element na daną sekundę
     */
    int test=30;
    /**
     * Interwał czasu do generowania nowego jedzenia = 8 oznacza co 8s nowy element
     */
    int co_ile_nowe_jedzenie = 8;
    /**
     * Tablica przechowująca grafiki animowania biegacza
     */
    int[] fazy_biegu;
    /**
     * Określenie która faza biegu jest aktualna (animacja postaci)
     */
    int faza=0;
    /**
     * Ustawienie szybkości "nadlatującego" jedzenia
     */
    int Iszybkosc = 5;

    /**
     * Deklaracja i inicjalizacja zmiennych z ekranu
     * @param savedInstanceState Generowanie planszy
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bUp = (Button) findViewById(R.id.button2);
        bDown = (Button) findViewById(R.id.button);
        bStart = (Button) findViewById(R.id.button3);

        bUp.setVisibility(View.INVISIBLE);
        bDown.setVisibility(View.INVISIBLE);

        gracz = (ImageView) findViewById(R.id.imageView3);
        wypelnij_tablice_obrazkami();

        Tpunkty = (TextView) findViewById(R.id.textView);
        Tpoziom = (TextView) findViewById(R.id.textView2);

        drawLinia = (ImageView) findViewById(R.id.linia1);
        drawLinia2 = (ImageView) findViewById(R.id.linia2);
        drawLinia3 = (ImageView) findViewById(R.id.linia3);

        serca[0] = (ImageView) findViewById(R.id.serce1);
        serca[1] = (ImageView) findViewById(R.id.serce2);
        serca[2] = (ImageView) findViewById(R.id.serce3);

        timer_txt = (TextView) findViewById(R.id.timer_tekst);

        fazy_biegu = new int[]{R.drawable.phase1, R.drawable.phase2, R.drawable.phase3, R.drawable.phase4, R.drawable.phase5};

        Pierwszy_poziom();
    }

    /**
     * Stworzenie layoutu i umieszczenie wszelkich potrzebnych elementów na planszy
     */
    public void Pierwszy_poziom() {
        timeLeft = 20000;

        linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.bg);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        drawLinia.setImageResource(R.drawable.bieznia);
        drawLinia2.setImageResource(R.drawable.bieznia);
        drawLinia3.setImageResource(R.drawable.bieznia);

        bStart.setLayoutParams(new LinearLayout.LayoutParams(450,300));
        bUp.setLayoutParams(new LinearLayout.LayoutParams(500,140));
        bDown.setLayoutParams(new LinearLayout.LayoutParams(500,140));

        Tpunkty.setLayoutParams(new LinearLayout.LayoutParams(300,120));
        Tpoziom.setLayoutParams(new LinearLayout.LayoutParams(300,120));
        drawLinia.setLayoutParams(new LinearLayout.LayoutParams(2000,50));
        drawLinia2.setLayoutParams(new LinearLayout.LayoutParams(2000,50));
        drawLinia3.setLayoutParams(new LinearLayout.LayoutParams(2000,50));

        gracz.setLayoutParams(new LinearLayout.LayoutParams(95,110));

        serca[0].setLayoutParams(new LinearLayout.LayoutParams(100,100));
        serca[1].setLayoutParams(new LinearLayout.LayoutParams(100,100));
        serca[2].setLayoutParams(new LinearLayout.LayoutParams(100,100));
        timer_txt.setLayoutParams(new LinearLayout.LayoutParams(300,100));

        ((ViewGroup) drawLinia.getParent()).removeView(drawLinia);
        ((ViewGroup) drawLinia2.getParent()).removeView(drawLinia2);
        ((ViewGroup) drawLinia3.getParent()).removeView(drawLinia3);

        ((ViewGroup) bUp.getParent()).removeView(bUp);
        ((ViewGroup) bDown.getParent()).removeView(bDown);
        ((ViewGroup) bStart.getParent()).removeView(bStart);
        ((ViewGroup) Tpunkty.getParent()).removeView(Tpunkty);
        ((ViewGroup) Tpoziom.getParent()).removeView(Tpoziom);

        ((ViewGroup) gracz.getParent()).removeView(gracz);

        ((ViewGroup) serca[0].getParent()).removeView(serca[0]);
        ((ViewGroup) serca[1].getParent()).removeView(serca[1]);
        ((ViewGroup) serca[2].getParent()).removeView(serca[2]);

        ((ViewGroup) timer_txt.getParent()).removeView(timer_txt);



        drawLinia.setX(-50);
        drawLinia.setY(100);
        drawLinia2.setX(-50);
        drawLinia2.setY(200);
        drawLinia3.setX(-50);
        drawLinia3.setY(300);

        bUp.setX(550);
        bUp.setY(320);
        bDown.setX(550);
        bDown.setY(320);

        Tpunkty.setX(199);
        Tpunkty.setY(120);
        Tpoziom.setX(200);
        Tpoziom.setY(80);

        bStart.setX(500);
        bStart.setY(-210);

        gracz.setX(75);
        gracz.setY(-820);

        serca[0].setX(1300);
        serca[0].setY(-500);
        serca[1].setX(1390);
        serca[1].setY(-600);
        serca[2].setX(1480);
        serca[2].setY(serca[2].getY()-700);

        timer_txt.setY(400);
        timer_txt.setX(300);


        linearLayout.addView(drawLinia);
        linearLayout.addView(drawLinia2);
        linearLayout.addView(drawLinia3);

        linearLayout.addView(bUp);
        linearLayout.addView(bDown);
        linearLayout.addView(Tpunkty);
        linearLayout.addView(Tpoziom);
        linearLayout.addView(bStart);

        linearLayout.addView(gracz);

        linearLayout.addView(serca[0]);
        linearLayout.addView(serca[1]);
        linearLayout.addView(serca[2]);

        linearLayout.addView(timer_txt);

        setContentView(linearLayout);
    }

    /**
     * Metoda odpowiedzialna za operacje po zakończeniu czasu - przygotowanie nowego poziomu
     */
    public void Koniec_czasu() {
        bStart.setVisibility(View.VISIBLE);
        bStart.setEnabled(true);
        bDown.setEnabled(false);
        bUp.setEnabled(false);
        Ipoziom++;
        Tpoziom.setText("Kliknij START by przejść dalej");
        for(int i=0;i<=elementow_ekran;i++) {
            obiekt[i].zdj_jedzenia.setVisibility(View.GONE);
        }
        timeLeft=30000;
        Iszybkosc+=2;
        co_ile_nowe_jedzenie--;
        nowy = true;
        if(Ipoziom == 6) Koniec_gry();
    }

    /**
     * Metoda wypełniająca tablice zdrowe_obrazki oraz niezdrowe_obrazki grafikami
     */
    public void wypelnij_tablice_obrazkami() {
        zdrowe_obrazki = new int[]{R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4, R.drawable.g5, R.drawable.g6, R.drawable.g7, R.drawable.g8, };
        niezdrowe_obrazki = new int[]{R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8, };
    }

    /**
     * Metoda sprawdzajaca czy czas juz dobiegl konca
     */
    public void startStop() {
        if (isTimerRun)
            stopTimer();
        else
            startTimer();
    }

    /**
     * Metoda rozpoczynajaca odliczanie w dol dla licznika czasu
     */
    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 50) {
            /**
             * Co każdą sekundę wywołaj metodę updateTimer()
             * @param millisUntilFinished milisekundy
             */
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            /**
             * Po zakończeniu czasu
             */
            @Override
            public void onFinish() {
                isTimerRun = false;
                stopTimer();

                Koniec_czasu();
            }
        }.start();
        isTimerRun = true;
    }

    /**
     * Metoda, ktora co kazdy dany interwał czasu, aktualizuje licznik
     */
    public void updateTimer() {
        int sec = (int) (timeLeft / 1000) % 60;


        String timeLeftText;
        timeLeftText = "" + sec;

        timer_txt.setText(timeLeftText);
        //System.out.println(timeLeft + " " + time + sec);

        int tor_gracza;
        if(gracz.getY() == 860) tor_gracza=2;

        for(int i=0;i<=elementow_ekran;i++) {
            if(obiekt[i].zdj_jedzenia.getX() > (-100)) {
                obiekt[i].zdj_jedzenia.setX(obiekt[i].zdj_jedzenia.getX() - Iszybkosc);
            }
            else pominiete_jedzenie(i);

            if(Math.abs(obiekt[i].zdj_jedzenia.getX() - gracz.getX()) <50 && Math.abs(obiekt[i].zdj_jedzenia.getY() - gracz.getY()) < 30)
            {
                kolizja_jedzenie_biegacz(i);
            }
        }

        Tpunkty.setText("Punkty: " + Ipunkty);
        Tpoziom.setText("Poziom: " + Ipoziom);

        if(timeLeft%2==0)
            animuj_gracz();

        if(sec % co_ile_nowe_jedzenie == 0 && timeLeft!=30000 && nowy) {
            nowyElement();
            nowy = false;
            test = sec;
        }

        if(sec<test) nowy = true;

    }

    /**
     * Metoda zatrzymujaca stoper, gdy czas sie skonczy lub uzytkownik przejdzie do kolejnego poziomu
     */
    public void stopTimer() {
        countDownTimer.cancel();
        isTimerRun = false;
    }

    /**
     * Metoda generująca 3 pierwsze elementy jedzeniowe na planszy oraz uruchamiająca czas na dany poziom
     * 1 poziom ma 20s, każdy kolejny 30s
     * @param v
     */
    public void Start_Click(View v)
    {
        int a = 0;
        bUp.setEnabled(true);
        bDown.setEnabled(true);
        bStart.setEnabled(false);
        bStart.setVisibility(View.INVISIBLE);
        bUp.setVisibility(View.VISIBLE);
        bDown.setVisibility(View.VISIBLE);

        for(int i=0;i<Ipoziom+2;i++) {
            int losuj_bieznie = random.nextInt(3);
            int losuj_polozenie = random.nextInt(1200) + 500;
            int losuj_obrazek = random.nextInt(8);
            obiekt[i] = new Jedzenie();
            obiekt[i].zdj_jedzenia = new ImageView(this);
            if(Ipoziom==1) {
                obiekt[i].zdj_jedzenia.setImageResource(zdrowe_obrazki[losuj_obrazek]);
                obiekt[i].zdrowe = true;
            }
            else {
                int losuj_zdrowe = random.nextInt(2);
                if (losuj_zdrowe==0) {
                    obiekt[i].zdj_jedzenia.setImageResource(zdrowe_obrazki[losuj_obrazek]);
                    obiekt[i].zdrowe = true;
                }
                else {
                    obiekt[i].zdrowe = false;
                    obiekt[i].zdj_jedzenia.setImageResource(niezdrowe_obrazki[losuj_obrazek]);
                }
            }
            obiekt[i].zdj_jedzenia.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 85));

            linearLayout.addView(obiekt[i].zdj_jedzenia);
            int wartosc = losuj_polozenie;

            if(i>1) {
                for(int j=0;j<i;j++)
                {
                    if (Math.abs(obiekt[j].zdj_jedzenia.getX() - losuj_polozenie) < 150) {
                        losuj_polozenie = random.nextInt(1200) + 500;
                        j=-1;
                        a=0;
                    }
                    else {
                        a++;
                        wartosc = losuj_polozenie;
                        if(a==i) break;
                    }
                }
            }

            obiekt[i].bieznia = losuj_bieznie;
            obiekt[i].zdj_jedzenia.setY(obiekt[i].zdj_jedzenia.getY()-bieznie_wys[losuj_bieznie]-(i*83));
            obiekt[i].zdj_jedzenia.setX(obiekt[i].zdj_jedzenia.getX()+wartosc);
            a++;
        }

        elementow_ekran=Ipoziom+1;
        startStop();
        updateTimer();
    }

    /**
     * Gdy element zniknie z planszy (zostanie zebrany przez gracza lub dojdzie do końca bieżni), następuje ustawienie go w nowym miejscu i na nowej bieżni
     * @param id id danego elementu
     * @param czyZdrowe przekazanie zmiennej wylosowanego typu jedzenia (zdrowe/niezdrowe)
     */
    public void dodaj_element_po_zniknieciu(int id, int czyZdrowe)
    {
        int losuj_bieznie = random.nextInt(3);
        int losuj_polozenie = random.nextInt(1200)+500;
        int losuj_obrazek = random.nextInt(8);
        if(czyZdrowe==0) {
            obiekt[id].zdj_jedzenia.setImageResource(zdrowe_obrazki[losuj_obrazek]);
            obiekt[id].zdrowe = true;
        } else {
            obiekt[id].zdj_jedzenia.setImageResource(niezdrowe_obrazki[losuj_obrazek]);
            obiekt[id].zdrowe = false;
        }

        int wartosc = losuj_polozenie;
        int a=0;

            for(int j=0;j<elementow_ekran;j++) {
                if (losuj_bieznie == obiekt[id].bieznia && Math.abs(obiekt[j].zdj_jedzenia.getX() - losuj_polozenie) < 60) {
                    losuj_polozenie = random.nextInt(1200) + 500;
                    j = -1;
                    a = 0;
                } else {
                    a++;
                    wartosc = losuj_polozenie;
                    if (a == elementow_ekran) break;
                }
            }
        obiekt[id].zdj_jedzenia.setY(0);
        obiekt[id].zdj_jedzenia.setY(pozycjonowanie[losuj_bieznie]);
        obiekt[id].zdj_jedzenia.setX(obiekt[id].zdj_jedzenia.getX() + wartosc);
    }

    /**
     * Metoda odpowiadająca za tworzenie i dodanie do planszy nowego elementu
     * Pętla for zabezpiecza przed dodawaniem elementów blisko siebie
     */
    public void nowyElement() {
        int a =0;
        elementow_ekran++;
        //System.out.println("Jedzenia: "+elementow_ekran);
        int losuj_bieznie = random.nextInt(3);
        int losuj_polozenie = random.nextInt(1200) + 500;
        int losuj_obrazek = random.nextInt(8);
        int losuj_zdrowe = random.nextInt(2);
        obiekt[elementow_ekran] = new Jedzenie();
        obiekt[elementow_ekran].zdj_jedzenia = new ImageView(this);
        if (losuj_zdrowe==0) {
            obiekt[elementow_ekran].zdj_jedzenia.setImageResource(zdrowe_obrazki[losuj_obrazek]);
            obiekt[elementow_ekran].zdrowe = true;
        }
        else {
            obiekt[elementow_ekran].zdj_jedzenia.setImageResource(niezdrowe_obrazki[losuj_obrazek]);
            obiekt[elementow_ekran].zdrowe = false;
        }
        obiekt[elementow_ekran].zdj_jedzenia.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 85));

        linearLayout.addView(obiekt[elementow_ekran].zdj_jedzenia);
        int wartosc = losuj_polozenie;

        for(int j=0;j<elementow_ekran-1;j++)
        {
            if (Math.abs(obiekt[j].zdj_jedzenia.getX() - losuj_polozenie) < 150) {
                losuj_polozenie = random.nextInt(1200) + 500;
                j=-1;
                a=0;
            }
            else {
                a++;
                wartosc = losuj_polozenie;
                if(a==elementow_ekran) break;
                }
            }


        obiekt[elementow_ekran].bieznia = losuj_bieznie;
        obiekt[elementow_ekran].zdj_jedzenia.setY(obiekt[elementow_ekran].zdj_jedzenia.getY()-bieznie_wys[losuj_bieznie]-(elementow_ekran*83));
        obiekt[elementow_ekran].zdj_jedzenia.setX(obiekt[elementow_ekran].zdj_jedzenia.getX()+wartosc);
        a++;
    }

    /**
     * Metoda odpowiedzialna za przesuwanie gracza na powyższe bieżnie.
     * Zabezpiecza przed wyjściem gracza poza planszę
     * @param v Na kliknięcie przycisku
     */
    public void Up_Click(View v)
    {
        if(gracz.getY()>10)
            gracz.setY(gracz.getY()-145);
    }
    /**
     * Metoda odpowiedzialna za przesuwanie gracza na poniższe bieżnie.
     * Zabezpiecza przed wyjściem gracza poza planszę
     * @param v Na kliknięcie przycisku
     */
    public void Down_Click(View v)
    {
        if(gracz.getY()<290)
            gracz.setY(gracz.getY()+145);
    }

    /**
     * Gdy biegacz "zbierze" dany rodzaj jedzenia
     * Dodaj/odejmij punkty i/lub życia
     * Przenieś obiekt w nowej postaci na początek bieżni
     * @param id id elementu
     */
    public void kolizja_jedzenie_biegacz(int id) {
        if(obiekt[id].zdrowe)
            Ipunkty+=15;
        else {
            Ipunkty-=10;
            if(Ipunkty == 0) Ipunkty = 0;
            Izycia--;
            if(Izycia <= 0) Koniec_gry();
            serca[Izycia].setVisibility(View.INVISIBLE);

        }
        int losuj_czyZdrowe = 0;
        Random r = new Random();
        losuj_czyZdrowe = r.nextInt(2);
        dodaj_element_po_zniknieciu(id, losuj_czyZdrowe);
    }

    /**
     * Metoda, która po "nie zebraniu" obiektu przez gracza umieszcza go z powrotem na początek bieżni
     * @param id
     */
    public void pominiete_jedzenie(int id) {
        int losuj_czyZdrowe = 0;
        Random r = new Random();
        losuj_czyZdrowe = r.nextInt(2);
        dodaj_element_po_zniknieciu(id, losuj_czyZdrowe);
    }

    /**
     * Gdy koniec gry (koniec żyć/dotarcie do poziomu 6) przejście do nowego okna
     */
    public void Koniec_gry() {
        Intent myIntent = new Intent(MainActivity.this, Koniec_gry.class);
        myIntent.putExtra("SCORE", Ipunkty); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    /**
     * Metoda animująca bieg gracza
     */
    public void animuj_gracz() {
        faza++;
        if(faza>=5) faza = 0;
        gracz.setImageResource(fazy_biegu[faza]);
    }

}