package cn.xyz.chaos.validator.utils.beans.resolver;

public interface Resolver {

	/**
	 * Extract the index value from the property expression or -1.
	 * 
	 * @param expression The property expression
	 * @return The index value or -1 if the property is not indexed
	 * @throws IllegalArgumentException If the indexed property is illegally
	 *             formed or has an invalid (non-numeric) value
	 */
	int getIndex(String expression);

	/**
	 * Extract the map key from the property expression or <code>null</code>.
	 * 
	 * @param expression The property expression
	 * @return The index value
	 * @throws IllegalArgumentException If the mapped property is illegally formed
	 */
	String getKey(String expression);

	/**
	 * Return the property name from the property expression.
	 * 
	 * @param expression The property expression
	 * @return The property name
	 */
	String getProperty(String expression);

	/**
	 * Indicates whether or not the expression
	 * contains nested property expressions or not.
	 * 
	 * @param expression The property expression
	 * @return The next property expression
	 */
	boolean hasNested(String expression);

	/**
	 * Indicate whether the expression is for an indexed property or not.
	 * 
	 * @param expression The property expression
	 * @return <code>true</code> if the expresion is indexed,
	 *         otherwise <code>false</code>
	 */
	boolean isIndexed(String expression);

	/**
	 * Indicate whether the expression is for a mapped property or not.
	 * 
	 * @param expression The property expression
	 * @return <code>true</code> if the expresion is mapped,
	 *         otherwise <code>false</code>
	 */
	boolean isMapped(String expression);

	/**
	 * Extract the next property expression from the
	 * current expression.
	 * 
	 * @param expression The property expression
	 * @return The next property expression
	 */
	String next(String expression);

	/**
	 * Remove the last property expresson from the
	 * current expression.
	 * 
	 * @param expression The property expression
	 * @return The new expression value, with first property
	 *         expression removed - null if there are no more expressions
	 */
	String remove(String expression);

}
