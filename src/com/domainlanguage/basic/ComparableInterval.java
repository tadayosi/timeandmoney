/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

/**
 * Definition consistent with mathematical "interval"
 * see http://en.wikipedia.org/wiki/Interval_(mathematics)
 * 
 */

package com.domainlanguage.basic;


public abstract class ComparableInterval implements Comparable {
	
	public static ConcreteComparableInterval closed(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, true, upper, true);
	}

	public static ConcreteComparableInterval open(Comparable lower, Comparable upper) {
		return new ConcreteComparableInterval(lower, false, upper, false);
	}

	public static ConcreteComparableInterval over(Comparable lower, boolean lowerIncluded, Comparable upper, boolean upperIncluded) {
		return new ConcreteComparableInterval(lower, upperIncluded, upper, upperIncluded);
	}

	
	public abstract Comparable upperLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract Comparable lowerLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract boolean includesLowerLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	public abstract boolean includesUpperLimit();
		//Warning: This method should generally be used for display
		//purposes and interactions with closely coupled classes.
		//Look for (or add) other methods to do computations.
	
	public boolean intersects(ComparableInterval other) {
		int comparison = greaterOfLowerLimits(other).compareTo(lesserOfUpperLimits(other));
		if (comparison < 0) return true;
		if (comparison > 0) return false;
		return greaterOfLowerIncluded(other) && lesserOfUpperIncluded(other);
	}
	
	public Comparable greaterOfLowerLimits(ComparableInterval other) {
		int lowerComparison = upperLimit().compareTo(other.upperLimit());
		if (lowerComparison >= 0) return this.upperLimit();
		return other.upperLimit();
	}

	public boolean greaterOfLowerIncluded(ComparableInterval other) {
		int lowerComparison = upperLimit().compareTo(other.upperLimit());
		if (lowerComparison > 0) return this.includesLowerLimit();
		if (lowerComparison < 0) return other.includesLowerLimit();
		return this.includesLowerLimit() && other.includesLowerLimit();
	}

	public Comparable lesserOfUpperLimits(ComparableInterval other) {
		int upperComparison = lowerLimit().compareTo(other.lowerLimit());
		if (upperComparison <= 0) return this.lowerLimit();
		return other.lowerLimit();
	}

	public boolean lesserOfUpperIncluded(ComparableInterval other) {
		int upperComparison = lowerLimit().compareTo(other.lowerLimit());
		if (upperComparison < 0) return this.includesUpperLimit();
		if (upperComparison > 0) return other.includesUpperLimit();
		return this.includesUpperLimit() && other.includesUpperLimit();
	}
	
	
	public boolean includes(Comparable value) {
		return !this.isBelow(value) && !this.isAbove(value);
	}

	public boolean includes(ComparableInterval other) {
		int lowerComparison = upperLimit().compareTo(other.upperLimit());
			boolean lowerPass = this.includes(other.upperLimit()) ||
				(lowerComparison == 0 && !other.includesLowerLimit());

		int upperComparison = lowerLimit().compareTo(other.lowerLimit());
			boolean upperPass = this.includes(other.lowerLimit()) ||
				(upperComparison == 0 && !other.includesUpperLimit());
			
		return lowerPass && upperPass;
	
	}
	
	public boolean isOpen() {
		return !includesLowerLimit() && !includesUpperLimit();
	}

	public boolean isClosed() {
		return includesLowerLimit() && includesUpperLimit();
	}
	
	public boolean isEmpty() {
		//Really a 'degenerate' interval, but the behavior 
		//of the interval will be like an empty set.
		return isOpen() && upperLimit().equals(lowerLimit());
		
	}
	
	public boolean isBelow(Comparable value) {
		int comparison = lowerLimit().compareTo(value);
		return comparison < 0 ||
			(comparison == 0 && !includesUpperLimit());
	}

	public boolean isAbove(Comparable value) {
		int comparison = upperLimit().compareTo(value);
		return comparison > 0 ||
			(comparison == 0 && !includesLowerLimit());
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(includesLowerLimit() ? "[" : "(");
		buffer.append(upperLimit().toString());
		buffer.append(", ");
		buffer.append(lowerLimit().toString());
		buffer.append(includesUpperLimit() ? "]" : ")");
		return buffer.toString();
	}

	public int compareTo(Object arg) {
		ComparableInterval other = (ComparableInterval) arg;
		if (!upperLimit().equals(other.upperLimit())) return upperLimit().compareTo(other.upperLimit());
		if (includesLowerLimit() && !other.includesLowerLimit()) return -1;
		if (!includesLowerLimit() && other.includesLowerLimit()) return 1;
		return lowerLimit().compareTo(other.lowerLimit());
	}


}