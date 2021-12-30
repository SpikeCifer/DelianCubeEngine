package interestingnessengine;

import java.util.ArrayList;

import result.Cell;
import result.Result;

public class GoalBasedIntentionalRelevance implements IInterestingnessMeasureWithHistory{

	private ArrayList<Cell> detailedQueryCube;
	private ArrayList<Cell> novelAreaOfInterest;
	private ArrayList<Cell> coveredAreaOfInterest;
	private ArrayList<Cell> userGoals;
	
	public double computeMeasure(IHistoryInput inputManager) {
		
		coveredAreaOfInterest = new ArrayList<Cell>();
		Result res = inputManager.getCurrentQueryResult();
		detailedQueryCube = new ArrayList<Cell>();
		for(Cell c: res.getCells()) {
			detailedQueryCube.add(c);
		}

		novelAreaOfInterest = new ArrayList<Cell>();
		novelAreaOfInterest.addAll(detailedQueryCube);

		userGoals = inputManager.getQueryGoals();
		for (int i=0; i<detailedQueryCube.size(); i++) {
			Cell c = detailedQueryCube.get(i);
			for (int j=0; j<userGoals.size(); j++) {
				if(testifCellsHaveEqualSignatures(c.getDimensionMembers(),userGoals.get(j).getDimensionMembers())){
					coveredAreaOfInterest.add(detailedQueryCube.get(j));
					novelAreaOfInterest.remove(detailedQueryCube.get(j));
					break;
				}
			}
		}
		
		return (double) coveredAreaOfInterest.size() / (double) (coveredAreaOfInterest.size() + (double) novelAreaOfInterest.size());

	}
	
	public boolean testifCellsHaveEqualSignatures(ArrayList<String> c1, ArrayList<String> c2) {
		if(c1.toString().equals(c2.toString())) {
			return true;
		}
		else{
			return false;
		}
	}

}