import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Forms implements Iterable<Form> {
	
	
	private ArrayList<Form> forms;
	
	public Forms(int dimension) {
		//Determine forms
		determineForms(dimension);
	}
	
	
	
	private void determineForms(int dimension) {
		
		ArrayList<Integer> factors = factorize(dimension);
		
		if (dimension != 9 && dimension % 2 == 1) {
			factors = new ArrayList<Integer>();
		}
		
		
		ArrayList<Form> formsRaw = new ArrayList<Form>();
		
		// Base couples
		for (int i = 0; i < factors.size(); i++) {
			int product = 1;
			for (int j = 0; j < factors.size(); j++) {
				if (j != i) {
					product *= factors.get(j);
				}
			}
			// Direct
			formsRaw.add(new Form(factors.get(i), product));
			// Inverse
			formsRaw.add(new Form(product, factors.get(i)));
		}
		
		//Clearing
		ArrayList<Form> formsElaborated = new ArrayList<Form>();
		for (int i = 0; i < formsRaw.size(); i++) {
			boolean insert = true;
			for (int j=0; j < formsElaborated.size(); j++) {
				if (formsRaw.get(i).equals(formsElaborated.get(j))) {
					insert = false;
				}
			}
			if (insert) {
				formsElaborated.add(formsRaw.get(i));
			}
		}
		
		//Sorting
		Collections.sort(formsElaborated, new Comparator<Form>() {
		    @Override
		    public int compare(Form o1, Form o2) {
		        return o2.compareTo(o1);
		    }
		});
		
		// Create forms array
		forms = new ArrayList<Form>(4 * (1 + formsElaborated.size()));
		
		
		forms.add(new Form(1, dimension));
		for (int i = 0; i < formsElaborated.size(); i++) {
			forms.add(formsElaborated.get(i));
		}
		
		forms.add(new Form(dimension, 1));
		//for (int i = formsElaborated.size(); i > 0; i--) {
		for (int i = 0; i < formsElaborated.size(); i++) {
			Form form = formsElaborated.get(i);
			forms.add(new Form(1+ form.getColumns(), -1 -form.getRows()));
		}
		
		forms.add(new Form(1, -dimension));
		for (int i = formsElaborated.size(); i > 0; i--) {
			Form form = formsElaborated.get(i-1);
			forms.add(new Form(-1 -form.getColumns(), -1 -form.getRows()));
		}
		
		forms.add(new Form(-dimension, 1));
		for (int i = 0; i < formsElaborated.size(); i++) {
			Form form = formsElaborated.get(i);
			forms.add(new Form(-1 -form.getColumns(), 1+ form.getRows()));
		}
		
	}
	
	private ArrayList<Integer> factorize(int number) {
		int n = number;
		ArrayList<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        
        return factors;
	}



	@Override
	public Iterator<Form> iterator() {
		// TODO Auto-generated method stub
		return forms.iterator();
	}
	
	

}
