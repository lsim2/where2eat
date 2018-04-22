package edu.brown.cs.mldm.yelp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Restaurant {
	private String id;
	private String name;
	private Map<String, Object> location; // need to get rid of Object? 
	private String price;
	private List<Map<String, String>> categories;
	private double rating;
	private Map<String, String> coordinates;
	private String is_closed;
	private String open_at;
	
	
	public Restaurant(){
		
	}
	
	/**
	 * Returns the unique business ID of the restaurant. 
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Returns the name of the restaurant. 
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * This method will return the location of the restaurant: City, State (might get rid of this)
	 * @return
	 */
//	public String getLocation(){
//		return location;
//	}
	
	/**
	 * Returns an array of doubles of size 2 representing the lat/lon coordinates of the restaurant. 
	 * [lat, lon]
	 * @return
	 */
	public double[] getCoordinates(){
		
		double[] coords = new double[2];
		coords[0] = Double.parseDouble(coordinates.get("latitude"));
		coords[1] = Double.parseDouble(coordinates.get("longitude"));
		return coords;
	}
	
	/**
	 * Returns a number between 1 and 4 depending on how expensive the restaurant is. 
	 * 1 - $
	 * 2 - $$
	 * 3 - $$$
	 * 4 - $$$$
	 * @return
	 */
	public int getPrice(){
		if(price.length()>0){
			return price.length();
		} 
		return 0;
	}
	/**
	 * Returns a set of the categories that the restaurant satisfies. eg: italian, mexican, indpak, etc...
	 * @return
	 */
	public Set<String> getCategories(){
		Set<String> cat = new HashSet<>();
		for(Map<String, String> rest: categories){
			cat.add(rest.get("alias"));
			//cat.add(rest.get("title"));
			//System.out.println(rest.get("alias"));
			//System.out.println(rest.get("title"));
			
		}
		return cat;
	}
	
/**
 * Returns a double between 0-5 depending on how well the restaurant is rated, 0 is worst, 5 is best. 
 */
	public double getRating(){
		return rating;
	}
	
	public String getAddress(){
		String addr = "";
		for(String str: (List<String>)location.get("display_address")){
			addr = addr + " " + str;
		}
		return addr;
	}
	
	public boolean isOpen(){
		if(is_closed.equals("false")){
			return true;
		} else{
			return false;
		}
	}
	
	public String getOpenAt(){
		return open_at;
	}
	
	public Set<String> getRestrictions(){
		Set<String> result = new HashSet<>();
		Set<String> restrictions = new HashSet<>(Arrays.asList(new String[]{"vegan", "vegetarian", "gluten_free","halal", "kosher"}));
		Set<String> all = this.getCategories();
		
		for(String cat: all){
			for(String res: restrictions){
				if(cat.equals(res)){
					System.out.println(res);
					result.add(res);
				}
			}
		}
		return result;
	}
	
	@Override 
	public String toString(){
		return "ID" + id + "Name: " + name;
	}
	
}
