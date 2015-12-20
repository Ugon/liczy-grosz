package pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes;

import java.util.function.BooleanSupplier;

/**
 * @author Wojciech Pachuta.
 */
public interface IColorfulValidatingNode {

    void setValidationSupplier(BooleanSupplier supplier);

    void triggerValidation();

}
