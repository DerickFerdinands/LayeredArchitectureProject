package lk.ijse.pos.bo;


import lk.ijse.pos.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public SuperBO getBO(BOTypes type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case INCOME_REPORTS:
                return new IncomeReportsBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case ITEM_MOVEMENT:
                return new ItemMovementBOImpl();
            case MANAGE_ORDER:
                return new ManageOrderBOImpl();
            case PLACE_ORDER:
                return new PlaceOrderBOImpl();
            case USER_HOME:
                return new UserHomeBOImpl();
            case ADMIN_HOME:
                return new AdminHomeBOImpl();
            default:
                return null;
        }
    }

    public enum BOTypes {
        CUSTOMER, INCOME_REPORTS, ITEM, ITEM_MOVEMENT, MANAGE_ORDER, PLACE_ORDER, USER_HOME, ADMIN_HOME
    }
}
