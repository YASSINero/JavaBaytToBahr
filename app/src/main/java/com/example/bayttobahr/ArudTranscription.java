package com.example.bayttobahr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("SpellCheckingInspection")
public class ArudTranscription {
    private ArudTranscription() {
        throw new AssertionError();
    }//Enforces non-instantiability

    private final static char[] tanweens = { 'ً'/*'\u064B'*/,'ٌ'/*'\u064C'*/,'ٍ'/*'\u064D'*/ };
    private final static char[] harakat = { 'َ'/*'\u064E'*/, 'ُ'/*'\u064F'*/ ,'ِ'/*'\u0650'*/ ,'ّ'/*'\u0651'*/ , 'ْ'/*'\u0652'*/};


    private static boolean isTanween(char harf)
    {
        for (char c : tanweens)
        {
            if(harf == c)
                return true;
        }
        return false;
    }

    private static boolean isHaraka(char harf)
    {
        for (char c : harakat)
        {
            if (harf == c)
                return true;
        }
        return false;
    }

    private static boolean isWayon(char harf) // wayon as in واي
    {
        /*'\u0649'*/
        return harf == 'ا'/*'\u0627'*/ || harf == 'و'/*'\u0648'*/ || harf == 'ي'/*'\u064A'*/ || harf == 'ى';
    }

    private static byte isHarf(char target) {
        switch (target) {
            case 'َ': case 'ُ': case 'ِ': case 'ْ': // haraka ignored
                return 0;

            case 'ً': case 'ٌ': case 'ٍ': 		 // there is two syllabs(harf + haraka) + (harf + soukun))
                return 1;

            case 'ّ':							 // same as previous case
                return 2;

            default:
                return 3;					 // isHarf

        }

    }


    public static String processShakl(String input, boolean isArabic)
    {

                StringBuilder sin = new StringBuilder(input);


        if (isArabic)
        {

//			|======8888======The Process======8888======|

                for (int i = 0; i < sin.length(); i++)
                {
                    // if(sin.charAt(i) == 'ة'/*'\u0629'*/ && i+1<sin.length() && (i+1 == sin.length()-1 ||
                    // sin.charAt(i+1) == ' '))
                    if (sin.charAt(i) == 'ة'/*'\u0629'*/) // tiedTa2 process isn't perfect
                    {
                        if (i == sin.length() - 1)
                        {
                            sin.replace(i, i + 1, "ه"/*'\u0647'*/);
                            sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                        }
                        if (i + 1 < sin.length() - 1 && sin.charAt(i + 1) == 'ً'/*'\u064B'*/)
                        {
                            sin.replace(i, i + 1, "ت"/*'\u062A'*/);
                            sin.replace(i + 1, i + 2, "َ"/*'\u064E'*/);
                            sin.insert(i + 2, 'ن'/*'\u0646'*/);
                            sin.insert(i + 3, 'ْ'/*'\u0652'*/);
                        }
                    }

                    switch (sin.charAt(i))
                    {
                        case 'ى'/*'\u0649'*/: // short alif

                            // own's letter harf process
                            if (i == sin.length() - 1)
                                sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                            else {

//						if (sin.charAt(i-1) != '?' && isHaraka(sin.charAt(i-1)))
//						{
//							sin.replace(i-1, 1, 1, '?');
//						}	// haraka correction (soukoun insertion below)

                                if (sin.charAt(i - 1) != 'َ'/*'\u064E'*/ && !isHaraka(sin.charAt(i - 1))) {
                                    if (i + 1 < sin.length() && !isTanween(sin.charAt(i + 1))) {
                                        sin.insert(i, 'َ'/*'\u064E'*/);
                                        i++; // this fixes the 112 bug
                                    }
                                } // giving fatha to preceeding harf

                                if (i + 1 < sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i, i + 1, "َ"/*'\u064E'*/);
                                    sin.replace(i + 1, i + 2, "ن"/*'\u0646'*/);
                                    sin.insert(i + 2, 'ْ'/*'\u0652'*/);
                                } // tanween materialization if not in the end
                                else if (i + 1 == sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i + 1, i + 2, "ْ"/*'\u0652'*/);
                                } // replacing last tanween with soukoun
                                else {
                                    sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                                    // inserting soukoun
                                }
                                // AFTER INSERTING A wchar the wstring gets longer by 1
                                // i.e. sin.length() => sin.length()+1
                                // => the current i in the iteration is off by one

                            } // END own's letter harf process
                            break;

                        case 'ا'/*'\u0627'*/: // Alif

                            if (i == sin.length() - 1 || sin.charAt(i + 1) == ' ') // check last letter is alif
                            {
                                sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                                if (i-2 >= 0 && !isHaraka(sin.charAt(i - 1))) {
                                    if (sin.charAt(i - 1) == 'و'/*'\u0648'*/ && sin.charAt(i - 2) != ' ') // check waw's
                                    // surroundings
                                    {
                                        sin.insert(i, 'ْ'/*'\u0652'*/); // => it's waw ljam3
                                    } else {
                                        sin.insert(i, 'َ'/*'\u064E'*/);
                                    }
                                    i++;
                                }
                            } else {
//						if(i == sin.length() - 1 || sin.charAt(i+1) == ' ')	//check alif in the end
//
//							if(sin.charAt(i-1) == 'و'/*'\u0648'*/ && sin.charAt(i-2) != ' ')	//check waw preceding it
//							{
//								sin.insert( i, 'ْ'/*'\u0652'*/);	//=> it's waw ljam3
//								i++;
//							}
//

                                if (i-1 > 0 && sin.charAt(i - 1) != ' ' && !isTanween(sin.charAt(i + 1)) //verse starting with alif bug fix
                                        && !isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i, 'َ'/*'\u064E'*/);
                                    i++;
                                }

                                if (i + 1 == sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i + 1, i + 2, "ْ"/*'\u0652'*/);
                                    sin.insert(i, 'َ'/*'\u064E'*/);
                                    i++;
                                } // replacing last tanween with soukoun
                                else if (i + 1 < sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i, i + 1, "َ"/*'\u064E'*/);
                                    sin.replace(i + 1, i + 2, "ن"/*'\u0646'*/);
                                    sin.insert(i + 2, 'ْ'/*'\u0652'*/);
                                } // tanween materialization if not in the end

                            }
                            break;
                        case 'ي'/*'\u064A'*/:
                        case /*'\u0648'*/'و':

                            if (i == 0)
                                continue; // Continue because arabic doesnt start with a saak'in
                            if (i + 2 < sin.length()) {
                                if (sin.charAt(i + 1) == 'ى'/*'\u0649'*/ && !isTanween(sin.charAt(i + 2))) {
                                    sin.insert(i + 1, 'َ'/*'\u064E'*/);
                                }

                                if (!isHaraka(sin.charAt(i + 1)) && !isHaraka(sin.charAt(i - 1))
                                        && sin.charAt(i - 1) != ' ') {
                                    sin.insert(i + 1, 'ْ'/*'\u0652'*/);

                                    sin.insert(i, sin.charAt(i) == /*'\u0648'*/'و' ? 'ُ'/*'\u064F'*/ : 'ِ'/*'\u0650'*/);
                                    i++;
                                } // giving haraka to preceeding harf according to cond.
                            }
                            else if (i == sin.length() - 1) {
                                sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                                if (!isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i, sin.charAt(i) == 'و'/*'\u0648'*/ ? 'ُ'/*'\u064F'*/ : 'ِ'/*'\u0650'*/);
                                    i++;
                                }
                            }
                            break;


                        case 'ٍ'/*'\u064D'*/:
                        case 'ٌ'/*'\u064C'*/:
                            if (i == sin.length() - 1) {
                                // this line replaces tanween by haraka
                                sin.replace(i, i + 1, sin.charAt(i) == 'ٍ'/*'\u064D'*/ ? "ِ"/*'\u0650'*/ : "ُ"/*'\u064F'*/);
                                if (sin.charAt(i - 1) == 'ة'/*'\u0629'*/) { // Ta2 changes if not in the end
                                    sin.replace(i - 1, i, "ت"/*'\u062A'*/); // by ta2 mabsouta
                                }
                                sin.insert(i + 1, sin.charAt(i) == 'ِ'/*'\u0650'*/ ? 'ي'/*'\u064A'*/ : 'و'/*'\u0648'*/);
                                sin.insert(i + 2, 'ْ'/*'\u0652'*/);
                            } else {
                                sin.replace(i, i + 1, sin.charAt(i) == 'ٍ'/*'\u064D'*/ ? "ِ"/*'\u0650'*/ : "ُ"/*'\u064F'*/);
                                sin.insert(i + 1, 'ن'/*'\u0646'*/);
                                sin.insert(i + 2, 'ْ'/*'\u0652'*/);
                            }
                            break;

                        case 'ّ'/*'\u0651'*/:
                            if (isWayon(sin.charAt(i + 1))) // ابّان
                            {
                                sin.replace(i, i + 1, "ْ"/*'\u0652'*/);
                                sin.insert(i + 1, sin.charAt(i - 1));
                                switch (sin.charAt(i + 2)) {
                                    case 'ي'/*'\u064A'*/:
                                    case 'و'/*'\u0648'*/:
                                        sin.insert(i + 2, sin.charAt(i + 2) == 'و'/*'\u0648'*/ ? 'ُ'/*'\u064F'*/ : 'ِ'/*'\u0650'*/);
                                        break;
                                    case 'ا'/*'\u0627'*/:
                                    case 'ى'/*'\u0649'*/:
                                        sin.insert(i + 2, 'َ'/*'\u064E'*/);
                                        break;
                                    default:

                                }
                            } else {
                                sin.replace(i, i + 1, "ْ"/*'\u0652'*/);
                                sin.insert(i + 1, sin.charAt(i - 1));
                            }
                            break;

                        default:
                            //System.out.println("Not amongst processed cases!");
                    }
                }
                //Todo Remember to multithread the regex work


        }
        return sin.toString();
    }


    private static final String[] ishara = { "هَذَاْ", "هَذِهِ", "هَذَاْنِ", "هَذَيْنِ", "هَؤُلَاْءِ" };
    private static final String[] jalala = {"اْللهُ", "اْللهِ", "اْللهَ", "اْللهُمْمَ"};	//<-if word is at start of verse won't match
    private static final String[] amr = {"عَمْرُنْو", "عمرُوْ", "عمرُنْو"};
    private static final String[] oul = {"أُوْلِيْ", "أُوْلُوْ", "أُوْلَاْءِ", "أُوْلَاْتِ", "َأُوْلَاْئِك"};

//  Patterns for the words above in both cases; isolated & connected
    private static final Pattern[] patt =
            {
                    Pattern.compile("\\W?(\\S{0,2}?)?("+ishara[0]+"|"+ishara[1]+"|"+ishara[2]+
                            "|"+ishara[3]+"|"+ishara[4]+")\\W?"),
                    Pattern.compile("\\W?(\\S{0,2}?)(لَكِنْ|لَكِنْنَ)\\S{0,4}?\\W?"),
                    //"\W?(\S{0,2}?)(لَكِنْ|لَكِنَّ)\S{0,4}?\W?"
                    Pattern.compile("\\W?(\\S{0,2}?)?("+jalala[0]+"|"+jalala[1]+"|"+
                            jalala[2]+"|"+jalala[3]+")\\W?"),
                    Pattern.compile("\\W?(\\S{0,2}?)?("+amr[0]+"|"+amr[1]+"|"
                            +amr[2]+")\\W?"),
                    Pattern.compile("\\W?(\\S{0,2}?)?("+oul[0]+"|"+oul[1]+"|"+oul[2]+
                            "|"+oul[3]+"|"+oul[4]+")\\W?"),
                    Pattern.compile("\\W?(\\S{0,2}?)(أُوْلَئِك)\\S{0,4}?\\W?")
            };


    //Process responsible of handling special words after shakl process
    public static String specialWordsProcess(StringBuilder target) {

        if(target.length() != 0)
        {

            String tempStr = target.toString();

            for (int i = 0; i < patt.length; i++)
            {
                Matcher match = patt[i].matcher(tempStr);

                if(match.matches()) {
                    //Using Groups is a must for manipulating string at correct index
                    System.out.println("Full match: " + match.group(0));
                    System.out.println("Target Match: " + match.group(2) + "\t Of position " + match.start(2));
                    byte startPos = (byte) match.start(2), endPos = (byte) match.end(2);

                    switch (i) {

                        // Both cases need same alif addition
                        case 0: case 1:
                            System.out.println("Case: addition" );
                            target.insert(startPos + 2, "اْ");
                            /*
                             *	\//Addition
                             *		case "هَذَاْ": case "هَذِهِ": case "هَذَاْنِ": case "هَذَيْنِ": case "هَؤُلَاْءِ":
                             *		break;
                             */
                            break;

                        case 2:
                            System.out.println("Case: jalala");
                            target.insert(startPos + 3, 'ْ');
                            target.insert(startPos + 5, 'َ');
                            target.insert(startPos + 6, "اْ");
                            /*
                             *	\//Addition
                             *		case "اْللهُ": case "اْللهِ": case "اْللهَ": case "اْللهُمْمَ":
                             * 		break;
                             */
                            break;

                        case 3: //Todo Adapt case to group index
                            System.out.println("Case: amr");
                            target.delete(endPos - 2, endPos);
                            /*
                             * \//removal
                             * 		case "عَمْرُنْو": case "عمرُوْ": case "عمرُنْو":
                             * 		break;
                             */
                            break;

                        case 4:
                            System.out.println("Case: oul");
                            target.delete(startPos + 2, startPos + 4); // removes(وْ)
                            /*
                             *	\//removal
                             *	case "أُوْلِيْ": case "أُوْلُوْ": case "أُوْلَاْءِ": case "أُوْلَاْتِ": case "َأُوْلَاْئِك":
                             *	break;
                             */
                            break;

                        case 5:
                            System.out.println("Case: mix");
                            target.delete(startPos + 2, startPos + 4);
                            target.insert(startPos + 4, "اْ"); //after deleting 2 chars 4 is the offset
                            /*
                             * mixed
                             * 		case"أُوْلَئِكَ"://tempStr == "أُوْلَئِكَ" ? 6 is offset for addition
                             *
                             * 		break;
                             */
                            break;

                        default:
                            break;
                    }

                    //since the word matches, another iteration is futile
                    break;

                }

            }

        }
		return target.toString();
}


    //Process responsible of linking words that end and start with saakins
    public static String wordsLinkingProcess(String linkTrgt)
    {
        StringBuilder lnkStr = new StringBuilder(linkTrgt);



        return lnkStr.toString();
    }

    //Last process handling syllabification
    public static byte getNSyllabs(String sTrgt)
    {
        StringBuilder strgt = new StringBuilder(sTrgt);
        byte nSyllabs_verse = 0;

        for (int i = 0; i < strgt.length(); i++)
        {
            if (strgt.charAt(i) != ' ' && isHarf(strgt.charAt(i)) == 3) {
                nSyllabs_verse++;

                    /*//case2,1 should be removed bc this func is called only after shakl process
                    case 2:	// shadda
                        nSyllabs_verse++;
                        break;

                    case 1:	// tanween
                        // check if preceding char == alif "ًا" || short-Alif "ًى"
                        // if not: nSyllabs_verse++;
                        // else: do nothing
                        if(!(strgt.charAt(i-1) == 'ا' || strgt.charAt(i-1) == 'ى')) {
                            nSyllabs_verse++;
                        }
                        break;*/
            }

        }

        return nSyllabs_verse;
    }






}
