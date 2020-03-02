import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Math;

public class Client3 {
    public static ArrayList<String> codes = new ArrayList<String>();

    public static void main(String[] args) {

        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);// pour lire à partir du clavier

        try {
            /*
             * les informations du serveur ( port et adresse IP ou nom d'hote 127.0.0.1 est
             * l'adresse local de la machine
             */
            clientSocket = new Socket("10.21.4.77", 60000);

            // flux pour envoyer
            out = new PrintWriter(clientSocket.getOutputStream());
            // flux pour recevoir
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread envoyer = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            envoyer.start();

            Thread recevoir = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("Serveur : " + msg);
                            msg = in.readLine();
                            if(msg.contains("Quelle est la longueur de la chaîne de caractères suivante :")) {
                              System.err.println(msg);
                              int strtPos = msg.indexOf("\"")+1,
                                  endPos = msg.indexOf("\"", strtPos);
                              out.println(endPos - strtPos);
                              out.flush();
                            }
                            else if(msg.contains("Voici le code à conserver :")) {
                              String code = msg.substring(msg.indexOf("<") + 1, msg.length() - 2);
                              codes.add(code);

                            }
                            else if(msg.contains("Envoyez") && msg.contains("étoile")) {
                              int strtPos = msg.indexOf(" ") + 1;
                              int endPos = msg.indexOf(" ", strtPos);
                              String nbToString = msg.substring(strtPos, endPos);
                              System.out.println("msg : " + nbToString);
                              int nb = Integer.parseInt(nbToString);
                              System.out.println(nb);
                              StringBuilder text = new StringBuilder("*");
                              for(int i = 1; i < nb; i++)
                                text.append("*");
                              out.println(text.toString());
                              out.flush();
                            }
                            else if (msg.contains("Doublez le nombre")) {
                              String valToString = msg.substring(18);
                              int val = Integer.parseInt(valToString);
                              out.println(val * 2);
                              out.flush();
                            }
                            else if (msg.contains("Décodez la chaine suivante") && msg.contains(" par le code de César de clé :")) {
                              StringBuilder strBld = new StringBuilder();
                              int strtPos = msg.indexOf("\"") + 1;
                              int endPos = msg.indexOf("\"", strtPos);
                              String text = msg.substring(strtPos, endPos);
                              String keyToString = msg.substring(msg.indexOf(":") + 2);
                              int key = Integer.parseInt(keyToString);
                              for(int i = 0; i < text.length(); i++) {
                                char c = text.charAt(i);
                                int val = c + 26 - 'a';
                                val = (val - key) % 26;
                                c = (char)(val + 'a');
                                strBld.append(c);
                              }
                              out.println(strBld.toString());
                              out.flush();
                            }
                            else if (msg.contains("Donnez moi un nombre à 2 chiffres identiques dont la somme fait : ")) {
                              String sumToString = msg.substring(msg.indexOf(":") + 2);
                              int x=11;
                              int sum = Integer.parseInt(sumToString);
                              sum = sum /2;
                              sum = sum + sum*10;
                              out.println(Integer.toString(sum));
                              out.flush();
                            }
                            else if (msg.contains("Quelle est la ") && msg.contains(" lettre de l'alphabet en minuscule ")) {
                              int endPos = msg.indexOf("è", 14);
                              String posToString = msg.substring(14, endPos);
                              System.out.println("'" + posToString + "'");
                              int pos = Integer.parseInt(posToString) -1;
                              out.println((char)(pos + 'a'));
                              out.flush();
                            }
                            else if (msg.contains("Merci de renvoyer le mot suivant : ")) {
                              StringBuilder strBld = new StringBuilder();
                              strBld.append(msg.substring(36));
                              strBld.deleteCharAt(strBld.length() - 1);
                              out.println(strBld.toString());
                              out.flush();
                            }
                            else if (msg.contains("Envoyez moi 5 entiers de 3 chiffres séparés par des espaces et dont la somme fait ")) {
                              StringBuilder strBld = new StringBuilder();
                              strBld.append(Integer.toString(100) + " " + Integer.toString(100) + " " + Integer.toString(100) + " " + Integer.toString(100) + " ");
                              String valToString = msg.substring(82);
                              int val = Integer.parseInt(valToString);
                              val = val - 400;
                              strBld.append(Integer.toString(val));
                              out.println(strBld.toString());
                              out.flush();
                            }
                            else if (msg.contains("Quelle distance, en mètres, a parcouru un objet se déplacant à ") && msg.contains("pendant")) {
                              String vitesseToString = msg.substring(63, msg.indexOf(' ', 63));
                              int vitesse = Integer.parseInt(vitesseToString);
                              String tempsToString = msg.substring(msg.indexOf("pendant ") + 8, msg.indexOf(" ", msg.indexOf("pendant ") + 8));
                              int temps = Integer.parseInt(tempsToString);
                              out.println(Integer.toString(vitesse * 60 * 1000 * temps));
                              out.flush();
                            }
                            else if(msg.contains("Quel est le résultat du calcul suivant :")) {
                              String nb1ToString= msg.substring(42, msg.indexOf(')', 42));
                              int nb1 = Integer.parseInt(nb1ToString);
                              String nb2ToString= msg.substring(msg.indexOf('(', 42)+1, msg.indexOf(')', msg.indexOf('(', 42)));
                              int nb2 = Integer.parseInt(nb2ToString);
                              String signe = msg.substring(msg.indexOf(")") + 1, msg.indexOf(")") + 2);
                              int result = 0;
                              switch(signe) {
                                case "+":
                                  result = nb1 + nb2;
                                break;
                                case "-":
                                  result = nb1 - nb2;
                                break;
                                case "*":
                                  result = nb1 * nb2;
                                break;
                              }
                              out.println(result);
                              out.flush();
                            }
                            else if(msg.contains("Donnez un facteur premier de")) {
                              String nbToString= msg.substring(29);
                              int nb = Integer.parseInt(nbToString);
                              for (int i = 1; i < nb; i++) {
                                if ( i % 2 == 0 && i / 2 > 1){
                                  continue;
                                }
                                else if ( i % 3 == 0 && i / 3 > 1){
                                  continue;
                                }
                                else if ( i % 5 == 0 && i / 5 > 1){
                                  continue;
                                }
                                else if ( i % 7 == 0 && i / 7 >1){
                                  continue;
                                }
                                if(nb % i == 0 && nb / i > 1){
                                  System.out.println(i);
                                  out.println(i);
                                  out.flush();
                                  break;
                                }
                              }
                            }
                            else if(msg.contains("Est ce que le nombre ") && msg.contains("est premier ? (La réponse attendue est \"oui\" ou \"non\" en minuscule)")) {
                              String nbToString= msg.substring(21, msg.indexOf(" ", 21));
                              int nb = Integer.parseInt(nbToString);
                              boolean test = false;
                              for (int i = 2; i < nb; i++){
                                if(nb % i == 0){
                                  test = true;
                                }
                              }
                              if(test){
                                System.out.println("non");
                                out.println("non");
                                out.flush();
                              }
                              else{
                                System.out.println("oui");
                                out.println("oui");
                                out.flush();
                              }
                            }
                            else if(msg.contains("Combien fait ") && msg.contains("? (Le signe \"!\" signifie factorielle)")) {
                              String nbToString= msg.substring(13, msg.indexOf("!"));
                              int nb = Integer.parseInt(nbToString);
                              int result = 1;
                              for(int i = 2; i <= nb; i++) {
                                result *= i;
                              }
                              out.println(Integer.toString(result));
                              out.flush();
                            }
                            else if(msg.contains("Pour quelle valeur de x cette équation est-elle vraie ? ")) {
                              String multiToString= msg.substring(56, msg.indexOf("x", 56));
                              int multi = Integer.parseInt(multiToString);
                              String addToString= msg.substring(msg.indexOf("+") + 1, msg.indexOf("="));
                              int add = Integer.parseInt(addToString);
                              String resultToString= msg.substring(msg.indexOf("=") + 1);
                              int result = Integer.parseInt(resultToString);
                              int x = (result - add) / multi;
                              out.println(Integer.toString(x));
                              out.flush();
                            }
                            else if(msg.contains("Pour quelles valeurs de x et de y ces deux équations sont-elles vraies ? : ")) {
                              String alphaToString= msg.substring(75, msg.indexOf("x", 75));
                              int alpha = Integer.parseInt(alphaToString);
                              String betaToString= msg.substring(msg.indexOf("+", 75)+1, msg.indexOf("y", 75));
                              int beta = Integer.parseInt(betaToString);
                              String epsilonToString= msg.substring(msg.indexOf("y+", 75) + 2, msg.indexOf("=", 75));
                              int epsilon = Integer.parseInt(epsilonToString);
                              String deltaToString= msg.substring(msg.indexOf("=", 75) + 1, msg.indexOf(" ", 75));
                              int delta = Integer.parseInt(deltaToString);
                              delta = delta - epsilon;
                              int pos = msg.indexOf("et", 75) + 3;
                              String aToString= msg.substring(pos, msg.indexOf("x", pos));
                              int a = Integer.parseInt(aToString);
                              String bToString= msg.substring(msg.indexOf("+", pos) + 1, msg.indexOf("y", pos));
                              int b = Integer.parseInt(bToString);
                              String cToString= msg.substring(msg.indexOf("y+", pos) + 2, msg.indexOf("=", pos));
                              int c = Integer.parseInt(cToString);
                              String dToString= msg.substring(msg.indexOf("=", pos) + 1);
                              int d = Integer.parseInt(dToString);
                              c = d - c;
                              /*
                              x = de-bf / ad-bc
                              y = af-ce / ad-bc

                              det = ad-bc*/
                              System.out.println(alpha + "x + " + beta + "y + " + epsilon + " = " + delta);
                              System.out.println(a + "x + " + b + "y + " + c + " = " + d);
                              int x = (delta*b - beta * c) / (alpha * b - beta * a);
                              int y =  (alpha * c - delta * a)/ (alpha * b - beta * a);
                              System.out.println("x : " + x + " y : " + y);
                              out.println(x + " " + y);
                              out.flush();

                            }
                            else if(msg.contains("La chaine ") && msg.contains(" est-elle un palindrome ? (La réponse attendue est \"oui\" ou \"non\" en miniscule)")) {

                              String text = msg.substring(11, msg.indexOf("\"", 11));

                              int leftPos = 0;
                              int rightPos = text.length() - 1;
                              boolean palindrome=true;
                              while ((leftPos < rightPos) && palindrome) {
                                  if (text.charAt(leftPos) != text.charAt(rightPos)) {
                                      palindrome=false;
                                  }
                                  leftPos++;
                                  rightPos--;
                              }

                              if (palindrome) {
                                out.println("oui");
                                out.flush();
                              } else {
                                out.println("non");
                                out.flush();
                              }
                            }
                            else if(msg.contains("Quel est le PGCD de ")) {
                              String aToString = msg.substring(20, msg.indexOf(" ", 20));
                              int a = Integer.parseInt(aToString);
                              String bToString = msg.substring(msg.indexOf("et ") +3, msg.indexOf("?") - 1);
                              int b = Integer.parseInt(bToString);
                              int r,q=0;

                              for(;;) {
                                r=a%b;
                                q = (a-r)/b;
                                if (r==0) break;
                                a=b;
                                b=r;
                              }

                              out.println(Integer.toString(b));
                              out.flush();
                            }
                            else if(msg.contains("Quel est le PPCM de ")) {
                              String aToString = msg.substring(20, msg.indexOf(" ", 20));
                              int a = Integer.parseInt(aToString);
                              String bToString = msg.substring(msg.indexOf("et ") +3, msg.indexOf("?") - 1);
                              int b = Integer.parseInt(bToString);
                              int p=a*b;
                              while (a!=b) if (a<b) b-=a; else a-=b;
                              out.println(Integer.toString(p/a));
                              out.flush();
                            }
                            else if(msg.contains("Bravo ! Tu as répondu correctement à toutes les questions. Voici le code à conserver précieusement :")) {
                              System.out.println("Serveur : " + msg);
                              msg = in.readLine();
                              System.out.println("Serveur : " + msg);
                              out.println(msg.substring(1, msg.length() -1 ));
                              out.flush();
                              out.close();
                              clientSocket.close();
                              System.out.println("Serveur déconecté");
                              return;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
