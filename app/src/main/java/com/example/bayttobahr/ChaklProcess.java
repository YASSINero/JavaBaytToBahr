package com.example.bayttobahr;

public class ChaklProcess {

    private final static char tanweens[] = { 'ً','ٌ','ٍ' };
    private final static char harakat[] = { 'َ', 'ُ' ,'ِ' ,'ّ' , 'ْ'};


    private static boolean isTanween(char harf)
    {
        //return std::any_of(std::begin(abajad), std::end(abajad), [harf](final char h){return h == harf;});
        for (char c : tanweens)
        {
            if(harf == c)
                return true;
        }
        return false;
    }

    private static boolean isHaraka(char harf)
    {
        //return std::any_of(std::begin(harakat), std::end(harakat), [harf](final char h) {return h == harf; });
        for (char c : harakat)
        {
            if (harf == c)
                return true;
        }
        return false;
    }

    private static boolean isWayon(char harf) // wayon as in واي
    {
        if (harf == 'ا' || harf == 'و' || harf == 'ي' || harf == 'ى')
            return true;

        return false;
    }

    public static String processShakl(String input, boolean isArabic)
    {

                StringBuilder sin = new StringBuilder(input);


        if (isArabic)
        {

//			|======8888======The Process======8888======|

                for (int i = 0; i < sin.length(); i++)
                {
                    // if(sin.charAt(i) == 'ة' && i+1<sin.length() && (i+1 == sin.length()-1 ||
                    // sin.charAt(i+1) == ' '))
                    if (sin.charAt(i) == 'ة') // tiedTa2 process isn't perfect
                    {
                        if (i == sin.length() - 1)
                        {
                            sin.replace(i, i + 1, "ه");
                            sin.insert(i + 1, 'ْ');
                        }
                        if (i + 1 < sin.length() - 1 && sin.charAt(i + 1) == 'ً')
                        {
                            sin.replace(i, i + 1, "ت");
                            sin.replace(i + 1, i + 2, "َ");
                            sin.insert(i + 2, 'ن');
                            sin.insert(i + 3, 'ْ');
                        }
                    }

                    switch (sin.charAt(i))
                    {
                        case 'ى': // short alif

                            // own's letter harf process
                            if (i == sin.length() - 1)
                                sin.insert(i + 1, 'ْ');
                            else {

//						if (sin.charAt(i-1) != '?' && isHaraka(sin.charAt(i-1)))
//						{
//							sin.replace(i-1, 1, 1, '?');
//						}	// haraka correction (soukoun insertion below)

                                if (sin.charAt(i - 1) != 'َ' && !isHaraka(sin.charAt(i - 1))) {
                                    if (i + 1 < sin.length() && !isTanween(sin.charAt(i + 1))) {
                                        sin.insert(i, 'َ');
                                        i++; // this fixes the 112 bug
                                    }
                                } // giving fatha to preceeding harf

                                if (i + 1 < sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i, i + 1, "َ");
                                    sin.replace(i + 1, i + 2, "ن");
                                    sin.insert(i + 2, 'ْ');
                                } // tanween materialization if not in the end
                                else if (i + 1 == sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i + 1, i + 2, "ْ");
                                } // replacing last tanween with soukoun
                                else {
                                    sin.insert(i + 1, 'ْ');
                                    // inserting soukoun
                                }
                                // AFTER INSERTING A wchar the wstring gets longer by 1
                                // i.e. sin.length() => sin.length()+1
                                // => the current i in the iteration is off by one

                            } // END own's letter harf process
                            break;

                        case 'ا': // Alif

                            if (i == sin.length() - 1 || sin.charAt(i + 1) == ' ') // check last letter is alif
                            {
                                sin.insert(i + 1, 'ْ');
                                if (!isHaraka(sin.charAt(i - 1))) {
                                    if (sin.charAt(i - 1) == 'و' && sin.charAt(i - 2) != ' ') // check waw's
                                    // surroundings
                                    {
                                        sin.insert(i, 'ْ'); // => it's waw ljam3
                                        i++;
                                    } else {
                                        sin.insert(i, 'َ');
                                        i++;
                                    }
                                }
                            } else {
//						if(i == sin.length() - 1 || sin.charAt(i+1) == ' ')	//check alif in the end
//
//							if(sin.charAt(i-1) == 'و' && sin.charAt(i-2) != ' ')	//check waw preceding it
//							{
//								sin.insert( i, 'ْ');	//=> it's waw ljam3
//								i++;
//							}
//

                                if (sin.charAt(i - 1) != ' ' && !isTanween(sin.charAt(i + 1))
                                        && !isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i, 'َ');
                                    i++;
                                }

                                if (i + 1 == sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i + 1, i + 2, "ْ");
                                    sin.insert(i, 'َ');
                                    i++;
                                } // replacing last tanween with soukoun
                                else if (i + 1 < sin.length() - 1 && isTanween(sin.charAt(i + 1))) {
                                    sin.replace(i, i + 1, "َ");
                                    sin.replace(i + 1, i + 2, "ن");
                                    sin.insert(i + 2, 'ْ');
                                } // tanween materialization if not in the end
                                else {
                                    // then alif is followed by a harf
                                    // if(sin[])
                                    sin.insert(i + 1, 'ْ');
                                    // sin.insert( i, '?');
                                }

//						/*
//
//						for (final auto c : harakat)
//						{
//							if (i > 0 && sin.charAt(i+1) != c)
//							{
//								sin.insert( i+1, '?');
//								break;
//							}
//						}*/

                            }
                            break;
                        case 'ي':
                        case 'و':

                            if (i == 0)
                                continue; // Continue because arabic doesnt start with a saak'in

                            if (sin.charAt(i + 1) == 'ى') {
                                if (!isTanween(sin.charAt(i + 2)))
                                    sin.insert(i + 1, 'َ');
                            } // WTF?? maybe sin.charAt(i+1) == alif ?

                            if (i + 1 < sin.length() && !isHaraka(sin.charAt(i + 1)) && !isHaraka(sin.charAt(i - 1))
                                    && sin.charAt(i - 1) != ' ') {
                                sin.insert(i + 1, 'ْ');

                                sin.insert(i, sin.charAt(i) == 'و' ? 'ُ' : 'ِ');
                                i++;
                            } // giving haraka to preceeding harf according to cond.
                            else if (i == sin.length() - 1) {
                                sin.insert(i + 1, 'ْ');
                                if (!isHaraka(sin.charAt(i - 1))) {
                                    sin.insert(i, sin.charAt(i) == 'و' ? 'ُ' : 'ِ');
                                    i++;
                                }
                            }

                            break;

                        case 'ٍ':
                        case 'ٌ':
                            if (i == sin.length() - 1) {
                                // this line replaces tanween by haraka
                                sin.replace(i, i + 1, sin.charAt(i) == 'ٍ' ? "ِ" : "ُ");
                                if (sin.charAt(i - 1) == 'ة') { // Ta2 changes if not in the end
                                    sin.replace(i - 1, i, "ت"); // by ta2 mabsouta
                                }
                                sin.insert(i + 1, sin.charAt(i) == 'ِ' ? 'ي' : 'و');
                                sin.insert(i + 2, 'ْ');
                            } else {
                                sin.replace(i, i + 1, sin.charAt(i) == 'ٍ' ? "ِ" : "ُ");
                                sin.insert(i + 1, 'ن');
                                sin.insert(i + 2, 'ْ');
                            }
                            break;

                        case 'ّ':
                            if (isWayon(sin.charAt(i + 1))) // ابّان
                            {
                                sin.replace(i, i + 1, "ْ");
                                sin.insert(i + 1, sin.charAt(i - 1));
                                switch (sin.charAt(i + 2)) {
                                    case 'ي':
                                    case 'و':
                                        sin.insert(i + 2, sin.charAt(i + 2) == 'و' ? 'ُ' : 'ِ');
                                        break;
                                    case 'ا':
                                    case 'ى':
                                        sin.insert(i + 2, 'َ');
                                        break;
                                    default:
                                        ;
                                }
                            } else {
                                sin.replace(i, i + 1, "ْ");
                                sin.insert(i + 1, sin.charAt(i - 1));
                            }
                            break;

                        default:
                            System.out.println("Not amongst processed cases!");
                    }
                }
                //Todo out of process->should export processed string to MainActivity


        }
        return sin.toString();
    }





}
