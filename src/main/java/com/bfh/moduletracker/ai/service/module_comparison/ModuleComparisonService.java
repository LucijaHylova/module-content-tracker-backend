
package com.bfh.moduletracker.ai.service.module_comparison;

import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonDeleteRequest;

public interface ModuleComparisonService {


    void deleteModuleComparison(ModuleComparisonDeleteRequest moduleComparisonDeleteRequest, String username);
}



