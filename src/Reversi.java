import java.util.ArrayList;

class Reversi {
    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final static int WIN = 0;
    private final static int LOSE = 1;
    private final static int DRAW = 2;

    private Player player;

    private int blackStones;
    private int whiteStones;
    private int noneStones;

    private final Stone[][] stones;

    Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player;
    }

    enum Player {
        PLAYER_1(Stone.Black, DRAW), PLAYER_2(Stone.White, DRAW);
        private final Stone stone;
        private int winOrLose;

        Player(Stone stone, int winOrLose) {
            this.stone = stone;
            this.winOrLose = winOrLose;
        }

        String getStone() {
            return stone.toString();
        }
    }

    enum Stone {
        White, Black, None
    }

    private enum Way {
        up(0, -1), down(0, 1), left(-1, 0), right(1, 0),
        upperRight(1, -1), upperLeft(-1, -1), bottomLeft(-1, 1), bottomRight(1, 1);

        private final int width;
        private final int height;

        Way(int width, int height) {
            this.height = height;
            this.width = width;
        }

    }


    Reversi() {
        stones = new Stone[BOARD_WIDTH][BOARD_HEIGHT];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                stones[i][j] = Stone.None;
            }
        }
        stones[BOARD_WIDTH / 2 - 1][BOARD_HEIGHT / 2 - 1] = Stone.White;
        stones[BOARD_WIDTH / 2 - 1][BOARD_HEIGHT / 2] = Stone.Black;
        stones[BOARD_WIDTH / 2][BOARD_HEIGHT / 2 - 1] = Stone.Black;
        stones[BOARD_WIDTH / 2][BOARD_HEIGHT / 2] = Stone.White;
        setPlayer(Player.PLAYER_1);
        countAllStones();
    }


    int play(int x, int y) {
        countAllStones();
        if (y >= 0 && x >= 0 && y < BOARD_HEIGHT && x < BOARD_WIDTH && stones[x][y] == Stone.None) {
            ArrayList<String> stoneList = getStoneList(x, y, getPlayer().stone);
            if (!stoneList.isEmpty()) {
                stones[x][y] = getPlayer().stone;
                turnStone(stoneList);
                countAllStones();
                isWinner();
                switch (getPlayer()) {
                    case PLAYER_1:
                        setPlayer(Player.PLAYER_2);
                        break;
                    case PLAYER_2:
                        setPlayer(Player.PLAYER_1);
                        break;
                }
                if (noneStones == 0) {
                    return 2;//終了
                } else if (blackStones == 0 || whiteStones == 0) {
                    return 3;
                } else if (!canPutStone(getPlayer().stone)) {
                    switch (getPlayer()) {
                        case PLAYER_1:
                            setPlayer(Player.PLAYER_2);
                            break;
                        case PLAYER_2:
                            setPlayer(Player.PLAYER_1);
                            break;
                    }
                    if (canPutStone(getPlayer().stone)) {
                        return 4;//詰みのためPLAYER変更
                    } else {
                        return 3;//詰みのため終了
                    }
                } else {
                    return 0;//置けた
                }
            } else {
                return 1;//置けない
            }
        } else {
            return 1;
        }
    }

    Stone getStoneLocated(int x, int y) {
        return stones[x][y];
    }

    int getBlackStones() {
        return blackStones;
    }

    int getWhiteStones() {
        return whiteStones;
    }

    String getWinner() {
        if (Player.PLAYER_1.winOrLose == WIN) {
            return "PLAYER_1";
        } else if (Player.PLAYER_2.winOrLose == WIN) {
            return "PLAYER_2";
        } else {
            return "Draw";
        }
    }


    private int tmp;

    private void searchTurnStone(int x, int y, Way way, ArrayList<String> stoneList, Stone puttingStone) {
        int searchLocationX = x + way.width;
        int searchLocationY = y + way.height;

        if (searchLocationY >= 0 && searchLocationY < BOARD_HEIGHT && searchLocationX >= 0 && searchLocationX < BOARD_WIDTH) {
            if (x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT
                    && stones[searchLocationX][searchLocationY] != Stone.None
                    && stones[searchLocationX][searchLocationY] != puttingStone) {
                stoneList.add(String.valueOf(searchLocationX) + (searchLocationY));
                tmp++;
                this.searchTurnStone(searchLocationX, searchLocationY, way, stoneList, puttingStone);
            } else {
                if (stones[searchLocationX][searchLocationY] == Stone.None) {
                    for (int i = 0; i < tmp; i++) {
                        stoneList.remove(stoneList.size() - 1);
                    }
                }
                tmp = 0;
            }
        } else {
            for (int i = 0; i < tmp; i++) {
                stoneList.remove(stoneList.size() - 1);
            }
            tmp = 0;
        }
    }

    private ArrayList<String> getStoneList(int x, int y, Stone puttingStone) {
        ArrayList<String> stoneList = new ArrayList<>();
        searchTurnStone(x, y, Way.up, stoneList, puttingStone);
        searchTurnStone(x, y, Way.down, stoneList, puttingStone);
        searchTurnStone(x, y, Way.right, stoneList, puttingStone);
        searchTurnStone(x, y, Way.left, stoneList, puttingStone);
        searchTurnStone(x, y, Way.bottomLeft, stoneList, puttingStone);
        searchTurnStone(x, y, Way.bottomRight, stoneList, puttingStone);
        searchTurnStone(x, y, Way.upperLeft, stoneList, puttingStone);
        searchTurnStone(x, y, Way.upperRight, stoneList, puttingStone);
        return stoneList;
    }

    private void countAllStones() {
        blackStones = 0;
        whiteStones = 0;
        noneStones = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                switch (stones[i][j]) {
                    case Black:
                        blackStones++;
                        break;
                    case White:
                        whiteStones++;
                        break;
                    case None:
                        noneStones++;
                }
            }
        }
    }

    private void turnStone(ArrayList<String> stoneList) {
        for (String s : stoneList) {
            stones[Character.getNumericValue(s.charAt(0))][Character.getNumericValue(s.charAt(1))] = getPlayer().stone;
        }
    }

    private void isWinner() {
        if (blackStones > whiteStones) {
            Player.PLAYER_1.winOrLose = WIN;
            Player.PLAYER_2.winOrLose = LOSE;
        } else if (blackStones < whiteStones) {
            Player.PLAYER_1.winOrLose = LOSE;
            Player.PLAYER_2.winOrLose = WIN;
        } else {
            Player.PLAYER_1.winOrLose = DRAW;
            Player.PLAYER_2.winOrLose = DRAW;
        }
    }

    private boolean canPutStone(Stone playerStone) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (stones[i][j] == Stone.None) {
                    if (!getStoneList(i, j, playerStone).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
