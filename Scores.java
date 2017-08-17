import java.io.*;

/**
 * Tracks high scores for each difficulty.
 *
 * @author Jake Koontz
 * Last update: 8/5/17
 */

public class Scores {
    // arrays containing high scores for each difficulty
    private double[] beginnerScores;
    private double[] intermediateScores;
    private double[] expertScores;

    /**
     * Initialize fields.
     */
    public Scores() {
        beginnerScores = new double[5];     // defaulted to top five times
        intermediateScores = new double[5];
        expertScores = new double[5];

        readScores();   // read scores from file
    }

    /**
     * Read scores from file and fill arrays.
     */
    private void readScores() {
        // try to open file containing scores
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\HighScores.txt"))) {
            String line = br.readLine();    // read first line in file

            int[] indices = new int[3];     // holds index of each array

            // get scores for each difficulty
            for (int i = 0; i < 3; i++) {
                // read each line in file
                while (line != null && line.length() > 0) {     // check that line is not empty
                    // check which difficulty score is for
                    if (i == 0) {
                        beginnerScores[indices[i]] = Double.parseDouble(line);  // add score to array
                        indices[i]++;   // increment correct index
                    } else if (i == 1) {
                        intermediateScores[indices[i]] = Double.parseDouble(line);
                        indices[i]++;
                    } else if (i == 2) {
                        expertScores[indices[i]] = Double.parseDouble(line);
                        indices[i]++;
                    }

                    line = br.readLine();   // read next line in file
                }

                line = br.readLine();   // read gap line between difficulties
            }

            br.close();     // close buffered reader
        } catch (IOException e) {
            e.printStackTrace();    // display stack trace in case of error
        }
    }

    /**
     * Write scores to file.
     */
    private void writeScores() {
        // try to open file containing scores
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\HighScores.txt"))) {
            // write scores for each difficulty
            for (int i = 0; i < 3; i++) {
                    int x = 0;  // sub-index to track each array

                    // check which difficulty score is for
                    if (i == 0) {
                        // check index and ensure it is an actual value
                        while (x < beginnerScores.length && beginnerScores[x] != 0) {
                            bw.write(beginnerScores[x] + "\n");     // write score to file
                            x++;
                        }

                        bw.newLine();   // write line to separate difficulties
                    } else if (i == 1) {
                        while (x < intermediateScores.length && intermediateScores[x] != 0) {
                            bw.write(intermediateScores[x] + "\n");
                            x++;
                        }

                        bw.newLine();
                    } else if (i == 2) {
                        while (x < expertScores.length && expertScores[x] != 0) {
                            bw.write(expertScores[x] + "\n");
                            x++;
                        }
                    }
            }

            bw.flush();     // remove anything left in the buffer
            bw.close();     // close buffered writer
        } catch (IOException e) {
            e.printStackTrace();    // display stack trace in case of error
        }
    }

    /**
     * See if score made the list and needs to be added.
     *
     * @param score score being tested
     * @param difficulty difficulty the score was attained on
     * @return boolean signifying whether or not score made the list
     */
    public boolean submitScore(double score, int difficulty) {
        // check difficulty
        if (difficulty == GameDriver.BEGINNER) {
            // check each score in the list
            for (int i = 0; i < beginnerScores.length; i++) {
                // add score if it makes the list
                if (beginnerScores[i] == 0 || score < beginnerScores[i]) {
                    int start = i;  // holds index where score needs to be added

                    // shift each score down one space in the list
                    for (i = beginnerScores.length - 1; i > start; i--)
                        beginnerScores[i] = beginnerScores[i - 1];

                    beginnerScores[start] = score;  // add score to list

                    writeScores();  // write score to list
                    return true;
                }
            }
        } else if (difficulty == GameDriver.INTERMEDIATE) {
            for (int i = 0; i < intermediateScores.length; i++) {
                if (intermediateScores[i] == 0 || score < intermediateScores[i]) {
                    int start = i;

                    for (i = intermediateScores.length - 1; i > start; i--)
                        intermediateScores[i] = intermediateScores[i - 1];

                    intermediateScores[start] = score;

                    writeScores();
                    return true;
                }
            }
        } else if (difficulty == GameDriver.EXPERT) {
            for (int i = 0; i < expertScores.length; i++) {
                if (expertScores[i] == 0 || score < expertScores[i]) {
                    int start = i;

                    for (i = expertScores.length - 1; i > start; i--)
                        expertScores[i] = expertScores[i - 1];

                    expertScores[start] = score;

                    writeScores();
                    return true;
                }
            }
        }

        return false;   // return false if custom difficulty or score did not make the list
    }

    /**
     * Obtain all scores for a respective difficulty.
     *
     * @param difficulty difficulty for which the scores are needed
     * @return String containing all scores at specified difficulty
     */
    public String getScores(int difficulty) {
        StringBuilder sb = new StringBuilder();

        if (difficulty == GameDriver.BEGINNER) {
            sb.append("Beginner:\n");

            // add each score to string
            for (double score : beginnerScores) {
                // check that spot is not empty
                if (score == 0)
                    break;

                sb.append("     ");
                sb.append(score);
                sb.append("\n");
            }
        } else if (difficulty == GameDriver.INTERMEDIATE) {
            sb.append("Intermediate:\n");

            for (double score : intermediateScores) {
                if (score == 0)
                    break;

                sb.append("     ");
                sb.append(score);
                sb.append("\n");
            }
        } else if (difficulty == GameDriver.EXPERT) {
            sb.append("Expert:\n");

            for (int i = 0; i < expertScores.length; i++) {
                if (expertScores[i] == 0)
                    break;

                sb.append("     ");
                sb.append(expertScores[i]);

                // add new line for each score but last
                if (i != expertScores.length - 1)
                    sb.append("\n");
            }
        }

        return sb.toString();
    }
}