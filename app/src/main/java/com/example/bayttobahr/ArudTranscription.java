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

    private static boolean isSukoon(char haraka) {
        return haraka == 'ْ'/*'\u0652'*/;
    }

    private static boolean isHarf(char target) {

        return !isHaraka(target);

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

                            } // END own's letter harf process
                            break;

                        case 'ا'/*'\u0627'*/: // Alif

                            //check alif-lam
                            int margin0 = (i!=0 && isHaraka(sin.charAt(i-1))) ? 2 : 1;
                            if(((i==0 || sin.charAt(i-1) == ' ')
                                    || (i == margin0 || sin.charAt(i-margin0-1) == ' '))
                                    && sin.charAt(sin.charAt(i+1) == 'َ'/*'\u064E'*/ ? i+2 : i+1) == 'ل') {
                                //Make a solution for:
                                // lam shakl basically bc alif must have it
                                // alif-lam that comes after a harf jar
                                int margin = sin.charAt(i+1) == 'ل' ? 2 : 3;
                                if(!isHaraka(sin.charAt(i+margin)) && sin.charAt(i+margin+1) != 'ّ'/*'\u0651'*/) {
                                    sin.insert(i + margin, 'ْ'/*'\u0652'*/);
                                }
                                if(!isHaraka(sin.charAt(i+1))) {
                                    sin.insert(i+1,'َ'/*'\u064E'*/);
                                }
                            }
                            else if (i == sin.length() - 1 || sin.charAt(i + 1) == ' ') // check last letter is alif
                            {
                                sin.insert(i + 1, 'ْ'/*'\u0652'*/);

                                if(i-2 >= 0 && !isHaraka(sin.charAt(i - 1))) {
                                    if (sin.charAt(i - 1) == 'و'/*'\u0648'*/ && sin.charAt(i - 2) != ' ') // check waw's surroundings
                                    {
                                        //Todo V Removes this alif and its sukoon
                                        //sin.delete(i, i + 2);
                                        sin.insert(i, 'ْ'/*'\u0652'*/); // => it's waw ljam3
                                    }
                                    else {
                                        sin.insert(i, 'َ'/*'\u064E'*/);
                                    }
                                    i++;
                                }
                                if(i-1 >= 0 && !isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i, 'َ'/*'\u064E'*/);
                                    i++;
                                }
                            }
                            else {

                                if (i-1 > 0 && sin.charAt(i - 1) != ' ' && !isTanween(sin.charAt(i + 1)) //verse starting with alif bug fix
                                        && !isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i + 1, 'ْ'/*'\u0652'*/);
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
//                                else {
//                                    // inserting soukoun
//                                }
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
                                else if(!isHaraka(sin.charAt(i + 1)) &&
                                        (sin.charAt(i - 1) == (sin.charAt(i) == /*'\u0648'*/'و' ? 'ُ'/*'\u064F'*/ : 'ِ'/*'\u0650'*/))) {
                                    sin.insert(i + 1, 'ْ'/*'\u0652'*/);
                                }
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
                            if (i + 1 < sin.length() && isWayon(sin.charAt(i + 1))) // ابّان
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
                                        if(!(i+3 < sin.length() && sin.charAt(i+3) == 'ً')) {
                                        sin.insert(i + 2, 'َ'/*'\u064E'*/);
                                        }
                                        break;

                                    default:

                                }
                            } else {
                                sin.replace(i, i + 1, "ْ"/*'\u0652'*/);
                                sin.insert(i + 1, sin.charAt(i - 1));
                            }
                            break;

                        case  'آ'/*'\u0622'*/:
                            sin.replace(i,i + 1, "ءَاْ");
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
//    private static final String[] jalala = {"\\W?(\\S{0,2}?)?(ا?[َْ]?لله[َُِ]?(?:مْمَ)?)\\s"};
    private static final String[] amr = {"عَمْرُنْو", "عمرُوْ", "عمرُنْو"};
    private static final String[] oul = {"أُوْلِيْ", "أُوْلُوْ", "أُوْلَاْءِ", "أُوْلَاْتِ", "َأُوْلَاْئِك"};

//  Patterns for the words above in both cases; isolated & connected
    private static final Pattern[] patt =
            {
                    Pattern.compile("\\W?(\\S{0,2}?)?("+ishara[0]+"|"+ishara[1]+"|"+ishara[2]+
                            "|"+ishara[3]+"|"+ishara[4]+")\\W?"),

                    Pattern.compile("\\W?(\\S{0,2}?)(لَكِنْ|لَكِنْنَ)\\S{0,4}?\\W?"),
                    //"\W?(\S{0,2}?)(لَكِنْ|لَكِنَّ)\S{0,4}?\W?"
                    Pattern.compile("\\W?(\\S{0,2}?)?(ا?[َْ]?لله[َُِ]?(?:مْمَ)?)(?:\\s|\\Z)"),

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

                //unlike .matches(), .lookingAt() doesn't require whole string to match
                //however, lookingAt() doesn't work if target is in the middle of string
                //must use .find() bc it works for all cases
                while(match.find()) {
                    //Using Groups is a must for manipulating string at correct index
//                    System.out.println("Full match: " + match.group(0));
//                    System.out.println("Target Match: " + match.group(2) + "\t Of position " + match.start(2));

                    int startPos = match.start(2), endPos = match.end(2);

                    switch (i) {

                        // Both cases need same alif addition
                        case 0: case 1:
                            System.out.println("Case: addition" );
                            target.insert(startPos + 2, "اْ");

                            break;

                        case 2:
                            int insertMargin = ((isHaraka(target.charAt(startPos + 1))) ? 3 : target.charAt(startPos) == 'ا' ? 2 : 1);
                            char firstHaraka = insertMargin == 1 ? 'ِ' : 'ْ';

                            System.out.println("Case: jalala");

                            target.insert(startPos + insertMargin, insertMargin == 1 ? firstHaraka + "لْ" : firstHaraka);
                            target.insert(startPos + insertMargin + (insertMargin == 1 ? 4 : 2), 'َ'/*'\u064E'*/);//5
                            target.insert(startPos + insertMargin + (insertMargin == 1 ? 5 : 3), "اْ");//6

                            break;

                        case 3:
                            System.out.println("Case: amr");
                            target.delete(endPos - 2, endPos);

                            break;

                        case 4:
                            System.out.println("Case: oul");
                            target.delete(startPos + 2, startPos + 4); // removes(وْ)

                            break;

                        case 5:
                            System.out.println("Case: mix");
                            target.delete(startPos + 2, startPos + 4);
                            target.insert(startPos + 4, "اْ"); //after deleting 2 chars 4 is the offset

                            break;

                        default:
                            break;
                    }//switch

                    tempStr = target.toString();
                    match = patt[i].matcher(tempStr);

                }//while


            }//for

        }
		return target.toString();
}


    //Process responsible of linking words with meeting saakins & other cases
    public static String wordsLinkingProcess(String linkTrgt)
    {
        StringBuilder lnkStr = new StringBuilder(linkTrgt);

        //Todo
        //  Force user to shakl alif-lam
        //  Remove meeting saakins
        //  Remove the connecting hamza
        //  Check new process

        if(linkTrgt.length() != 0)
            for(int i = 0; i< lnkStr.length(); i++) {
                if(i==0) {
                    int margin = isHaraka(lnkStr.charAt(i+1)) ? i+2 : i+1;
                    String prefix = lnkStr.charAt(margin+1) == 'ِ'/*'\u0650'*/ ? "اِ" : "اُ",
                            subPrefix = lnkStr.substring(i, i+7);

                    if(subPrefix.startsWith(prefix, margin)) {
                        lnkStr.delete(margin, margin+2);
                    }
                    else if(subPrefix.startsWith("اَل", margin)) {
                        lnkStr.delete(margin, isSukoon(lnkStr.charAt(margin+3)) ? margin+2 : margin+3);
                    }
                }
                else if(lnkStr.charAt(i) == ' ') {

                    boolean sukoon_i = isSukoon(lnkStr.charAt(i-1));

                    String harfAlifLam = lnkStr.substring(i+1, i+7);

                    boolean isMoonAlifLam = harfAlifLam.startsWith("اَلْ"),
                            isHarfMoonAlifLam = harfAlifLam.startsWith("اَلْ", isHaraka(harfAlifLam.charAt(1)) ? 2 : 1),
                            isAlifLam = harfAlifLam.contains("اَل"),

                            isConnectingHamza = harfAlifLam.startsWith("اِ") || harfAlifLam.startsWith("اُ"),
                            isHarfCnctHamza = (harfAlifLam.contains("اِ") || harfAlifLam.contains("اُ"))
                                    && harfAlifLam.charAt(0) != 'ا';

                    //case: harf then (alif-lam/connectingHamza) in start of _verse

                    //case: harf then alif-lam
                    if(isAlifLam && harfAlifLam.charAt(0) != 'ا'/*'\u0627'*/) {

                        int margin = isHaraka(harfAlifLam.charAt(1)) ? 3 : 2;

                        lnkStr.delete(i+margin, !isHarfMoonAlifLam ? i+margin+3 : i+margin+2);
                    }
                    else if(isAlifLam && harfAlifLam.charAt(0) == 'ا'/*'\u0627'*/) {

                        //case1: ma'd (wayon) is before alif-lam
                        int margin = isHaraka(lnkStr.charAt(i-1))? 2 : 1;
                        if(isWayon(lnkStr.charAt(i-margin))) {
                            lnkStr.delete(i+1, !isMoonAlifLam ? i+4 : i+3);
                            lnkStr.delete(i-margin, i);
                            i -= margin;
                        }
                        //case2: haraka is before alif-lam
                        else if(isHaraka(lnkStr.charAt(i-1)) && !isSukoon(lnkStr.charAt(i-1))) {
                            lnkStr.delete(i+1, !isMoonAlifLam ? i+4 : i+3);
                        }
                        else if(isSukoon(lnkStr.charAt(i-1)) && !isWayon(lnkStr.charAt(i-2))) {
                            lnkStr.setCharAt(i-1, 'ِ'/*'\u0650'*/);
                            lnkStr.delete(i+1, !isMoonAlifLam ? i+4 : i+3);
                        }
                    }
                    //Todo cases of connecting hamza
                    //  hamza must have only damma/kasra as haraka
                    //  ma'd before (space) hamza
                    //  haraka & sukoon cases before (space) hamza
                    //  harf then hamza
                    else if( isConnectingHamza && isWayon(lnkStr.charAt(i - (sukoon_i ? 2 : 1))) ) {
                        int margin = sukoon_i ? 2 : 1;
                        lnkStr.delete(i+1, i+3);
                        lnkStr.delete(i - margin, i);
                        i -= margin;
                    }
                    else if(isConnectingHamza && (isHaraka(lnkStr.charAt(i-1)) && !isSukoon(lnkStr.charAt(i-1)))) {
                        lnkStr.delete(i+1, i+3);
                    }
                    else if(isHarfCnctHamza) {
                        int margin = isHaraka(harfAlifLam.charAt(1)) ? 3 : 2;
                        lnkStr.delete(i+margin, i+margin+2);
                    }
                }
                //Todo check new logic:
                //  case for harf then (alif-lam/connectingHamza) in start of _verse
            }



        return lnkStr.toString();
    }

    //Last process handling syllabification
    public static byte getNSyllabs(String processedVerse)
    {
        byte nSyllabs_verse = 0;

        for (int i = 0; i < processedVerse.length(); i++) {
            if (processedVerse.charAt(i) != ' ' && isHarf(processedVerse.charAt(i))) {
                nSyllabs_verse++;
            }
        }
        return nSyllabs_verse;
    }

}
