/*
 * LifeCompanion AAC and its sub projects
 *
 * Copyright (C) 2014 to 2019 Mathieu THEBAUD
 * Copyright (C) 2020 to 2021 CMRRF KERPAPE (Lorient, France)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lifecompanion.config.data.common;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.*;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import org.lifecompanion.model.api.editaction.BaseEditActionI;
import org.lifecompanion.util.LCUtils;
import org.lifecompanion.config.data.control.ConfigActionController;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.lifecompanion.util.LCUtils.tolerantRound;

/**
 * Class to create binding with config action and fields in config UI.
 *
 * @author Mathieu THEBAUD <math.thebaud@gmail.com>
 */
public class LCConfigBindingUtils {

    // Class part : "UI Binding"
    //========================================================================
    public static <T> ChangeListener<Number> createSliderBindingWithScale(final int scale, final Slider slider, final ObjectProperty<T> modelProperty,
                                                                          final Function<T, ObservableNumberValue> propertyRetriever, final BiFunction<T, Number, BaseEditActionI> actionCreator) {
        //Change on field
        ChangeListener<Number> changeListenerField = (obs, ov, nv) -> {
            T model = modelProperty.get();
            //Fire action on change with keyboard
            if (!slider.isValueChanging() && model != null) {
                ObservableNumberValue numberValue = propertyRetriever.apply(model);
                double valueRoundNv = tolerantRound(nv.doubleValue(), scale);
                if (tolerantRound(numberValue.doubleValue(), scale) != valueRoundNv) {
                    ConfigActionController.INSTANCE.executeAction(actionCreator.apply(model, valueRoundNv));
                }
            }
        };
        slider.valueProperty().addListener(changeListenerField);
        //Fire action on end
        slider.valueChangingProperty().addListener((obs, ov, nv) -> {
            if (!nv) {
                changeListenerField.changed(null, null, tolerantRound(slider.valueProperty().getValue(), scale));
            }
        });
        //Create listener on model
        ChangeListener<Number> changeListenerModel = (obs, ov, nv) -> {
            if (tolerantRound(nv.doubleValue(), scale) != tolerantRound(slider.valueProperty().doubleValue(), scale)) {
                slider.adjustValue(tolerantRound(nv.doubleValue(), scale));
            }
        };
        return changeListenerModel;
    }

    //	public static <T> ChangeListener<Number> createSliderBinding(final Slider slider, final ObjectProperty<T> modelProperty,
    //			final Function<T, ObservableNumberValue> propertyRetriever, final BiFunction<T, Number, BaseConfigActionI> actionCreator) {
    //		return LCConfigBindingUtils.createSliderBindingWithScale(3, slider, modelProperty, propertyRetriever, actionCreator);
    //	}

    public static <T> ChangeListener<Number> createSliderBindingWithoutScale(final Slider slider, final ObjectProperty<T> modelProperty,
                                                                             final Function<T, ObservableNumberValue> propertyRetriever, final BiFunction<T, Number, BaseEditActionI> actionCreator) {
        return LCConfigBindingUtils.createSliderBindingWithScale(0, slider, modelProperty, propertyRetriever, actionCreator);
    }

    public static <T, K> ChangeListener<T> createSelectionModelBinding(final SelectionModel<T> selectionModel, final ObjectProperty<K> modelProperty,
                                                                       final Function<K, T> propertyRetriever, final BiFunction<K, T, BaseEditActionI> actionCreator) {
        return LCConfigBindingUtils.createSelectionModelBinding(selectionModel, modelProperty, propertyRetriever, t1 -> t1, t2 -> t2, actionCreator);
        //		//Bind combobox
        //		selectionModel.selectedItemProperty().addListener((obs, ov, nv) -> {
        //			K model = modelProperty.get();
        //			if (model != null) {
        //				T prop = propertyRetriever.apply(model);
        //				if (prop != nv) {
        //					BaseConfigActionI action = actionCreator.apply(model, nv);
        //					ConfigActionController.INSTANCE.executeAction(action);
        //				}
        //			}
        //		});
        //		//Create listener for model
        //		ChangeListener<T> changeListener = (obs, ov, nv) -> {
        //			if (selectionModel.getSelectedItem() != nv) {
        //				selectionModel.select(nv);
        //			}
        //		};
        //		return changeListener;
    }

    public static <T, K, V> ChangeListener<V> createSelectionModelBinding(final SelectionModel<T> selectionModel,
                                                                          final ObjectProperty<K> modelProperty, final Function<K, V> propertyRetriever, final Function<T, V> toModelConverter,
                                                                          final Function<V, T> toSelectionModeConverter, final BiFunction<K, V, BaseEditActionI> actionCreator) {
        //Bind combobox
        selectionModel.selectedItemProperty().addListener((obs, ov, nv) -> {
            K model = modelProperty.get();
            if (model != null) {
                T prop = toSelectionModeConverter.apply(propertyRetriever.apply(model));
                if (!LCUtils.safeEquals(prop, nv)) {
                    BaseEditActionI action = actionCreator.apply(model, toModelConverter.apply(nv));
                    ConfigActionController.INSTANCE.executeAction(action);
                }
            }
        });
        //Create listener for model
        ChangeListener<V> changeListener = (obs, ov, nv) -> {
            if (nv != null) {
                T selectionVal = toSelectionModeConverter.apply(nv);
                if (selectionModel.getSelectedItem() != selectionVal) {
                    selectionModel.select(selectionVal);
                }
            }
        };
        return changeListener;
    }

    public static <T> ChangeListener<Number> createIntegerSpinnerBinding(final Spinner<Integer> spinner, final ObjectProperty<T> modelProperty,
                                                                         final Function<T, ObservableValue<Number>> propertyRetriever, final BiFunction<T, Integer, BaseEditActionI> actionCreator) {
        //Field listener
        spinner.valueProperty().addListener((obs, ov, nv) -> {
            T model = modelProperty.get();
            if (model != null) {
                ObservableValue<Number> prop = propertyRetriever.apply(model);
                if (prop.getValue().intValue() != nv.intValue()) {
                    ConfigActionController.INSTANCE.executeAction(actionCreator.apply(model, nv));
                }
            }
        });
        //Model listener
        ChangeListener<Number> modelListener = (obs, ov, nv) -> {
            if (spinner.getValue().intValue() != nv.intValue()) {
                spinner.getValueFactory().setValue(nv.intValue());
            }
        };
        return modelListener;
    }

    public static <T> ChangeListener<Number> createDoubleSpinnerBinding(final Spinner<Double> spinner, final ObjectProperty<T> modelProperty,
                                                                        final Function<T, ObservableDoubleValue> propertyRetriever, final BiFunction<T, Double, BaseEditActionI> actionCreator) {
        return LCConfigBindingUtils.createDoubleSpinnerBindingWithCondition(spinner, modelProperty, propertyRetriever, actionCreator, null);
    }

    public static <T> ChangeListener<Number> createDoubleSpinnerBindingWithCondition(final Spinner<Double> spinner,
                                                                                     final ObjectProperty<T> modelProperty, final Function<T, ObservableDoubleValue> propertyRetriever,
                                                                                     final BiFunction<T, Double, BaseEditActionI> actionCreator, final Predicate<T> actionCondition) {
        //Field listener
        spinner.valueProperty().addListener((obs, ov, nv) -> {
            T model = modelProperty.get();
            if (model != null) {
                ObservableDoubleValue prop = propertyRetriever.apply(model);
                boolean cond = actionCondition != null ? actionCondition.test(model) : true;
                if (cond && tolerantRound(prop.getValue().doubleValue()) != tolerantRound(nv)) {
                    ConfigActionController.INSTANCE.executeAction(actionCreator.apply(model, nv));
                }
            }
        });
        //Model listener
        ChangeListener<Number> modelListener = (obs, ov, nv) -> {
            if (tolerantRound(spinner.getValue()) != tolerantRound(nv.doubleValue())) {
                spinner.getValueFactory().setValue(nv.doubleValue());
            }
        };
        return modelListener;
    }

    public static <T> ChangeListener<Number> createIntegerToDoubleSpinnerBinding(final Spinner<Double> spinner, final ObjectProperty<T> modelProperty,
                                                                                 final Function<T, ObservableIntegerValue> propertyRetriever, final double factor,
                                                                                 final BiFunction<T, Integer, BaseEditActionI> actionCreator) {
        //Field listener
        spinner.valueProperty().addListener((obs, ov, nv) -> {
            T model = modelProperty.get();
            if (model != null) {
                ObservableIntegerValue prop = propertyRetriever.apply(model);
                if (tolerantRound(prop.get() / factor) != tolerantRound(nv)) {
                    ConfigActionController.INSTANCE.executeAction(actionCreator.apply(model, (int) (nv * factor)));
                }
            }
        });
        //Model listener
        ChangeListener<Number> modelListener = (obs, ov, nv) -> {
            if (tolerantRound(spinner.getValue().doubleValue()) != tolerantRound(nv.doubleValue() / factor)) {
                spinner.getValueFactory().setValue(nv.doubleValue() / factor);
            }
        };
        return modelListener;
    }

    public static <K, T, E> ChangeListener<T> createSimpleBindingWithTransformer(final Property<T> fieldValueProperty,
                                                                                 final Function<T, T> transformer, final ReadOnlyObjectProperty<K> modelProperty, final Function<K, T> propertyRetriever,
                                                                                 final BiFunction<K, T, BaseEditActionI> actionCreator) {
        //Field listener
        fieldValueProperty.addListener((obs, ov, nv) -> {
            K model = modelProperty.get();
            if (model != null) {
                T prop = propertyRetriever.apply(model);
                nv = transformer != null ? transformer.apply(nv) : nv;
                if (prop != nv) {
                    ConfigActionController.INSTANCE.executeAction(actionCreator.apply(model, nv));
                }
            }
        });
        //Model listener
        return (obs, ov, nv) -> {
            nv = transformer != null ? transformer.apply(nv) : nv;
            if (fieldValueProperty.getValue() != nv) {
                fieldValueProperty.setValue(nv);
            }
        };
    }

    public static <K, T, E> ChangeListener<T> createSimpleBinding(final Property<T> fieldValueProperty, final ReadOnlyObjectProperty<K> modelProperty,
                                                                  final Function<K, T> propertyRetriever, final BiFunction<K, T, BaseEditActionI> actionCreator) {
        return LCConfigBindingUtils.createSimpleBindingWithTransformer(fieldValueProperty, null, modelProperty, propertyRetriever, actionCreator);
    }
    //========================================================================

    // Class part : "Bindings"
    //========================================================================
    public static <K, V> MapChangeListener<K, V> createBindMapValue(final ObservableList<V> list) {
        return (change) -> {
            V added = change.getValueAdded();
            if (added != null) {
                list.add(added);
            }
            V removed = change.getValueRemoved();
            if (removed != null) {
                list.remove(removed);
            }
        };
    }
    //========================================================================

}
