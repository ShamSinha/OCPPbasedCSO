package com.example.cso;

import javafx.beans.property.SimpleStringProperty;

public class GetVariableDataType {

        private final SimpleStringProperty attributeType;
        private final SimpleStringProperty attributeValue;
        private final SimpleStringProperty component;
        private final SimpleStringProperty variable;

        public GetVariableDataType(String component, String variable, String attributeType, String attributeValue) {
            this.attributeType = new SimpleStringProperty(attributeType);
            this.attributeValue = new SimpleStringProperty(attributeValue);
            this.component = new SimpleStringProperty(component);
            this.variable = new SimpleStringProperty(variable);
        }

        public String getAttributeType() {
            return attributeType.get();
        }

        public SimpleStringProperty attributeTypeProperty() {
            return attributeType;
        }

        public String getAttributeValue() {
            return attributeValue.get();
        }

        public SimpleStringProperty attributeValueProperty() {
            return attributeValue;
        }

        public String getComponent() {
            return component.get();
        }

        public SimpleStringProperty componentProperty() {
            return component;
        }

        public String getVariable() {
            return variable.get();
        }

        public SimpleStringProperty variableProperty() {
            return variable;
        }

}
