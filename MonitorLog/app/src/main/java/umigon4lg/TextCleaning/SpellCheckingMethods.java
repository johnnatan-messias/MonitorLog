/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 Copyright 2013 Clement Levallois
 Authors : Clement Levallois <clement.levallois@gephi.org>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s):

 Portions Copyrighted 2011 Gephi Consortium.
 */
package umigon4lg.TextCleaning;

import java.util.HashSet;
import java.util.Set;

//import javax.ejb.Stateless;
//import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import umigon4lg.Singletons.HeuristicsLoader;

//@Stateless [4LG]
public class SpellCheckingMethods {

    public SpellCheckingMethods() {
    	this.HLoader = HeuristicsLoader.getInstance(); //[4LG]
    }

    //@Inject [4LG]
    HeuristicsLoader HLoader;

    public String repeatedCharacters(String currTerm) {
        String toReturn = currTerm;
        Integer index = null;
        Set<RepeatedLetters> setRL = new HashSet();
        int count = 1;
        char[] chars = currTerm.toCharArray();
        char currChar;
        char previousChar = 0;
        for (int i = 0; i < chars.length; i++) {
            currChar = chars[i];
            if (i > 0) {
                previousChar = chars[i - 1];
            }
            if (previousChar == currChar && StringUtils.isAlpha(String.valueOf(previousChar))) {
                if (index == null) {
                    index = i - 1;
                }
                count++;

            } else {
                if (count > 1) {
                    setRL.add(new RepeatedLetters(previousChar, index, count));
                    count = 1;
                }
                index = null;

            }
            if (i == (chars.length - 1) && count > 1) {
                setRL.add(new RepeatedLetters(previousChar, index, count));

            }
        }

        boolean loop = true;
        int loopsCounter = 0;
        while (loop) {
            loopsCounter++;
            if (loopsCounter > 5) {
                break;
            }
            for (RepeatedLetters rl : setRL) {
                String letter = String.valueOf(rl.getCurrChar());
                String toReplace;
                String subs;
                String toBeReplaced;

                //if two same letters are found
                if (rl.getCount() > 1) {
                    toBeReplaced = currTerm.substring(rl.getIndex(), rl.getIndex() + rl.getCount());

                    ///if these are actually 3 letters or more, test if by replacing them by 2 letters we have a match in the heuristics
                    if (rl.getCount() > 2) {
                        toReplace = letter + letter;
                        subs = StringUtils.replace(toReturn, toBeReplaced, toReplace);
                        if (HLoader.getMapHeuristics().containsKey(subs.toLowerCase())) {
                            toReturn = subs;
                            loop = false;
                            break;
                        } else if (toReturn.endsWith(toReplace) && !toReturn.contains(" ")) {
                            toReturn = StringUtils.replace(toReturn, toBeReplaced, letter);
                            loop = true;
                            break;
                        }
                    }

                    // and maybe that if they are just one, this is a match too? (as in "boredd" meaning "bored")
//                    toReplace = letter;
//                    subs = StringUtils.replace(toReturn, toBeReplaced, toReplace);
//                    if (HLoader.getMapHeuristics().containsKey(subs.toLowerCase())) {
//                        toReturn = subs;
//                        loop = false;
//                        break;
//                    }
                } else {
                    loop = false;
                }
            }
        }
        return toReturn;
    }

    private class RepeatedLetters {

        private char currChar;
        private int index;
        private int count;

        public RepeatedLetters(char currChar, int index, int count) {
            this.currChar = currChar;
            this.index = index;
            this.count = count;
        }

        public char getCurrChar() {
            return currChar;
        }

        public int getIndex() {
            return index;
        }

        public int getCount() {
            return count;
        }
    }
}
