package matrix;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
	private Cell[][] cells;
	
	public Matrix(int sx, int sy){
		cells = new Cell[sx][sy];
		for(int i = 0; i < cells.length; i++){
			for(int n = 0; n < cells[0].length; n++){
				cells[i][n] = new Cell(i, n, false);
			}
		}
	}
	
	public Cell get(int x, int y){
		return  cells[x][y];
	}
	public Cell[] neighbors(int x, int y){
		if(x < 0 || y < 0 || x > cells.length-1 || y > cells.length-1){
			return null;
		}
		List<Cell> out = new ArrayList<Cell>();
		//LEFT TOP
		if(y > 0 && x > 0){
			 out.add(cells[x-1][y-1]);
		}
		//LEFT MIDDLE
		if(x > 0){
			 out.add(cells[x-1][y]);
		}
		//LEFT BOTTOM
		if(x > 0 && y < cells[0].length-1){
			out.add(cells[x-1][y+1]);
		}
		//MIDDLE TOP
		if(y > 0){
			out.add(cells[x][y-1]);
		}
		//MIDDLE BOTTOM
		if(y < cells[0].length-1){
			out.add(cells[x][y+1]);
		}
		//RIGHT TOP
		if(y > 0 && x < cells.length-1){
			out.add(cells[x+1][y-1]);
		}
		//RIGHT MIDDLE
		if(x < cells.length-1){
			out.add(cells[x+1][y]);
		}
		//RIGHT BOTTOM
		if(x < cells.length-1 && y < cells[0].length-1){
			out.add(cells[x+1][y+1]);
		}
		return out.toArray(new Cell[out.size()]);
	}
	
	public void noBombs() {
		for(int i = 0; i < cells.length; i++) {
			for(int n = 0; n < cells[0].length; n++) {
				cells[i][n].setBomb(false);
				cells[i][n].setClicked(false);
				cells[i][n].setMarkedBomb(false);
				cells[i][n].setMarkedQues(false);
				cells[i][n].setoBomb(false);
				cells[i][n].setWrong(false);
			}
		}
	}
	
	public void reset(int bombs) {
		noBombs();
		for(int i = 0; i < bombs; i++) {
			int x = (int) (Math.random() * rows());
			int y = (int) (Math.random() * columns());
			if(!cells[x][y].hasBomb()) {
				cells[x][y].setBomb(true);
			} else {
				i--;
			}
		}
		countBombs();
		
	}

	public boolean[][][] export() {
		boolean[][][] out = new boolean[cells.length][cells[0].length][];
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0; j < cells[0].length; j++) {
				Cell c = get(i, j);
				out[i][j] = new boolean[] {
						c.hasBomb(),
						c.isMarkedBomb(),
						c.isClicked(),
						c.isoBomb(),
						c.isWrong()
				};
			}
		}
		countBombs();
		return out;
	}
	
	private void countBombs(){
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[0].length; j++){
				int count = 0;
				Cell[] neigh = neighbors(i, j);
				if(neigh == null){
					continue;
				}
				for(Cell c: neigh){
					if(c.hasBomb()){
						count++;
					}
				}
				cells[i][j].setBombs(count);
			}
		}
	}
	
	public boolean input(boolean[][][] map) {
		boolean lost = false;
		Cell[][] tmp2 = new Cell[map.length][map[0].length];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				boolean [] tmp = map[i][j];
				tmp2[i][j] = new Cell(i, j, tmp[0]);
				tmp2[i][j].setMarkedBomb(tmp[1]);
				tmp2[i][j].setClicked(tmp[2]);
				tmp2[i][j].setoBomb(tmp[3]);
				tmp2[i][j].setWrong(tmp[4]);
				//System.out.println(map[i][j][2]);
				if(tmp[2] && tmp[0]){
					lost = true;
				}
			}
		}
		//System.out.println(Arrays.deepEquals(cells, tmp2) ? "equals": "not the same, changing");
		cells = tmp2;
		countBombs();
		return lost;
	}
	
	public int rows() {
		return cells.length;
	}
	public int columns() {
		return cells[0].length;
	}
}
