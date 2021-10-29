package model;

/**
 * @className: ErrorInfo
 * @description: TODO 类描述
 * @author: Chen Jiaying
 * @date: 2021/10/29
 **/
public class ErrorInfo {

    private Integer row;
    private Integer column;
    private String errorMessage;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
