package com.example.clientZeebe.common.enums;

public final class Const {

    public static final String Process_claim_policy_suffix = "claim";
    public enum PROCESS {
        Process_claim_policy(Process_claim_policy_suffix);
        public String processPrefix;

        private PROCESS(String processPrefix) {
            this.processPrefix = processPrefix;
        }

        public String getProcessPrefix() {
            return processPrefix;
        }
    }

    public enum MESSAGE {
        Message_madarek_claim
    }

    public enum TASK {
        CREATED,COMPLETED, CANCELED, FAILED
    }

    public enum RuleExecutionType {
        OBJ_TYPE;
        public static RuleExecutionType fromStringOrNull(String type) {
            try {
                return RuleExecutionType.valueOf(type);
            }catch (Exception e) {
                return null;
            }
        }
    }
}
