package com.rd.aicodegenerator.core.saver;

import cn.hutool.core.util.StrUtil;
import com.rd.aicodegenerator.ai.model.HtmlCodeResult;
import com.rd.aicodegenerator.exception.BusinessException;
import com.rd.aicodegenerator.exception.ErrorCode;
import com.rd.aicodegenerator.model.enums.CodeGenTypeEnum;

/**
 * HTML代码文件保存器
 *
 * @author rd
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML 代码不能为空");
        }
    }
}
