package com.cobra.db;

public class DBConstants {

	public static final String TABLE_MODULE = "tbl_module";
	public static final String TABLE_LABEL = "tbl_label";
	public static final String TABLE_TIME = "tbl_time";
	public static final String TABLE_BUCKET = "tbl_bucket";
	public static final String TABLE_BUCKET_MODULE = "tbl_bucket_module";
	public static final String TABLE_BUCKET_LABEL = "tbl_bucket_label";
	
	public static final String MODULE_FIELD_ID = "module_id";
	public static final String MODULE_FIELD_ADDRESS = "module_address";
	public static final String MODULE_FIELD_CHANNEL = "module_channel";
	public static final String MODULE_FIELD_TEMP = "module_temp";
	public static final String MODULE_FIELD_CUE_1 = "cue_1";
	public static final String MODULE_FIELD_CUE_2 = "cue_2";
	public static final String MODULE_FIELD_CUE_3 = "cue_3";
	public static final String MODULE_FIELD_CUE_4 = "cue_4";
	public static final String MODULE_FIELD_CUE_5 = "cue_5";
	public static final String MODULE_FIELD_CUE_6 = "cue_6";
	public static final String MODULE_FIELD_CUE_7 = "cue_7";
	public static final String MODULE_FIELD_CUE_8 = "cue_8";
	public static final String MODULE_FIELD_CUE_9 = "cue_9";
	public static final String MODULE_FIELD_CUE_10 = "cue_10";
	public static final String MODULE_FIELD_CUE_11 = "cue_11";
	public static final String MODULE_FIELD_CUE_12 = "cue_12";
	public static final String MODULE_FIELD_CUE_13 = "cue_13";
	public static final String MODULE_FIELD_CUE_14 = "cue_14";
	public static final String MODULE_FIELD_CUE_15 = "cue_15";
	public static final String MODULE_FIELD_CUE_16 = "cue_16";
	public static final String MODULE_FIELD_CUE_17 = "cue_17";
	public static final String MODULE_FIELD_CUE_18 = "cue_18";

	public static final String LABEL_FIELD_ID = "label_id";
	public static final String LABEL_FIELD_NAME = "label_name";
	
	public static final String TIME_FIELD_ID = "time_id";
	public static final String TIME_FIELD_TIME = "time";

	public static final String BUCKET_MODULE_FIELD_ID = "id";
	public static final String BUCKET_MODULE_FIELD_BUCKET_ID = "bucket_id";
	public static final String BUCKET_MODULE_FIELD_MODULE_ADDRESS = "module_address";

	public static final String BUCKET_LABEL_FIELD_ID = "id";
	public static final String BUCKET_LABEL_FIELD_BUCKET_ID = "bucket_id";
	public static final String BUCKET_LABEL_FIELD_LABEL_ID = "label_id";
	public static final String BUCKET_LABEL_FIELD_LABEL_NAME = "label_name";

	public static final String BUCKET_FIELD_ID = "bucket_id";
	public static final String BUCKET_FIELD_NAME = "bucket_name";
	public static final String BUCKET_FIELD_TIME = "bucket_time";
	public static final String BUCKET_FIELD_STATUS = "bucket_status";
	
	public static Boolean isPortrait = true;

	// LOGIN POST PARAMETERS
	public static final String POST_FIELD_LOGIN_USERNAME = "username";
	public static final String POST_FIELD_LOGIN_PASSWORD = "password";
	public static final String POST_FIELD_LOGIN_SPINNER = "Days";
	public static final String POST_FIELD_LOGIN_LAST_UNIX = "UNIXLAST";
	public static final String POST_FIELD_LOGIN_DO_LOGIN = "do_login";
	public static final String POST_FIELD_LOGIN_NO_REDIR = "noRedir";
	public static final String POST_FIELD_LOGIN_IS_APP = "is_app";
	public static final String POST_VALUE_LOGIN_NO_REDIR = "1";
	public static final String POST_VALUE_LOGIN_IS_APP = "1";
	public static final String POST_VALUE_LOGIN_DO_LOGIN = "1";
	public static final String LOGIN_RESP_FIELD_PARAM = "login";
	public static final String LOGIN_RESP_FIELD_PARAM_LOGIN_TAG = "<login>";
	public static final String HTTP_FIELD_PARAM = "http://";
	public static final String HTTPS_FIELD_PARAM = "https://";
	public static final String ALREADY_LOGIN_STATUS = "already_login";
	public static final String IS_LOGIN = "is_login";

	// JOB DETAIL PARAMETERS
	public static final String POST_VALUE_JOB_DETAIL_ACCEPT = "accept";
	public static final String POST_VALUE_JOB_DETAIL_APPLY = "apply";
	public static final String POST_VALUE_JOB_DETAIL_REMOVE_APPICATION = "removeApplication";
	public static final String POST_VALUE_JOB_DETAIL_REJECT = "reject";
	public static final String POST_FIELD_JOB_DETAIL_REFUSAL_REASON = "RefusalReason";
	public static final String POST_FIELD_JOB_DETAIL_ORDER_ID = "OrderID";
	public static final String POST_VALUE_JOB_DETAIL_PARAM_VALUE = "1";
	public static final String JOB_DETAIL_RESP_FIELD_PARAM = "<status>";
	public static final String JOB_DETAIL_JOB_START_FIELD_KEY = "Status";
	public static final String JOB_DETAIL_IS_REJECT_FIELD_KEY = "isReject";
	public static final String JOB_DETAIL_IS_INVALID_LOGIN_FIELD_KEY = "invalidLogin";

	// ORDER
	public static final String FIELD_ORDER_SET_ID = "SetID";
	public static final String JOB_LIST_RESP_FIELD_PARAM = "reports";

	// LocationParams
	public static final String POST_FIELD_LOC_USERID = "checkername";
	public static final String POST_FIELD_LOC_LAT = "lat";
	public static final String POST_FIELD_LOC_LONG = "long";

	// PRODUCT POST PARAMETERS
	public static final String POST_FIELD_PROD = "prod";
	public static final String POST_FIELD_PROD_ID = "-ProductID";
	public static final String POST_FIELD_PROD_NAME = "-ProductName";
	public static final String POST_FIELD_PROD_CODE = "-ProductCode";
	public static final String POST_FIELD_PROD_LOCID = "-ProdLocationID";
	public static final String POST_FIELD_PROD_LOC = "-Location";
	public static final String POST_FIELD_PROD_QUANTITY = "-Quantity";
	public static final String POST_FIELD_PROD_PRICE = "-Price";
	public static final String POST_FIELD_PROD_NOTE = "-Note";
	public static final String POST_FIELD_PROD_EXPIRATION = "-Expiration";

	// QUESTIONNAIRE POST PARAMETERS
	public static final String POST_FIELD_UNEMPTY_QUES_COUNT = "TotalAnswersSent";
	public static final String POST_FIELD_QUES_ORDER_ID = "OrderID";
	public static final String POST_FIELD_QUES_CRITFREETEXT = "CritFreeText";
	public static final String POST_FIELD_QUES_CRITSTARTLAT = "CritStartLat";
	public static final String POST_FIELD_QUES_CRITSTARTLONG = "CritStartLong";
	public static final String POST_FIELD_QUES_CRITENDLAT = "CritEndLat";
	public static final String POST_VALUE_QUES_CRITENDLONG = "CritEndLong";
	public static final String POST_VALUE_QUES_REPORTED_START_TIME = "reported_StartTime";
	public static final String POST_VALUE_QUES_REPORTED_FINISH_TIME = "reported_FinishTime";
	public static final String POST_VALUE_QUES_APP_VERSION = "android_appversion";
	public static final String POST_VALUE_QUES_APP_ACTUAL_VERSION = "4.07";
	public static final String QUES_RESP_FIELD_PARAM = "sets";
	public static final String QUES_RESP_FIELD_VALIDATION_DATAID = "ValidationDataID";
	public static final String QUES_RESP_FIELD_VALIDATION_ANSWERID = "ValidationAnswerID";

	// VALIDATION POST PARAMETERS
	public static final String VALITION_QUESTION_DATA_TABLE = "ValidationQuestionTbl";
	public static final String VALITION_QUESTION_TABLE = "ValidationQuestion";
	public static final String VALITION_ANSWER_TABLE = "ValidationQuesAnswer";
	public static final String UPLOAD_FILE_TABLE = "UploadFile";
	public static final String UPLOAD_FILe_MEDIAFILE = "MediaFile";
	public static final String UPLOAD_FILe_DATAID = "DataID";

	// SETTINGS PAGE
	public static final String SETTINGS_SYSTEM_URL_KEY = "SYSTEM_URL";
	public static final String SETTINGS_LANGUAGE_INDEX = "language_arr_index";
	public static final String[] SETTINGS_LOCALE_VAL_ARR = { "en", "sp", "ro",
			"iw", "ja", "pt", "hi" };
	public static final String SETTINGS_MODE_INDEX = "setting_mode";
	public static final String AUTOSYNC_CURRENT_TIME = "current_time";

	// QUESTIONNAIRE PAGE
	public static final String QUESTIONNAIRE_JUMPTO_LBL = "Jump to:";
	public static final String QUESTIONNAIRE_JUMPTO_NEXT_PAGE = "Next page";
	public static final String QUESTIONNAIRE_STAUS = "Status";
	// public static final String QUESTIONNAIRE_JUMPTO_NEXT_PAGE = "Next page";

	// DB FIELDS

	public static final String DB_TABLE_SUBMITSURVEY = "SubmitSurvey";
	public static final String DB_TABLE_SUBMITSURVEY_OID = "OrderID";
	public static final String DB_TABLE_SUBMITSURVEY_FT = "FreeText";
	public static final String DB_TABLE_SUBMITSURVEY_SLT = "StartLat";
	public static final String DB_TABLE_SUBMITSURVEY_SLNG = "StartLng";
	public static final String DB_TABLE_SUBMITSURVEY_ELT = "EndLat";
	public static final String DB_TABLE_SUBMITSURVEY_ELNG = "EndLng";
	public static final String DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME = "reported_FinishTime";
	public static final String DB_TABLE_SUBMITSURVEY_REPORTED_START_TIME = "reported_StartTime";
	public static final String DB_TABLE_SUBMITSURVEY_UNEMPTY_QUES_COUNT = "TotalAnswersSent";

	public static final String DB_TABLE_QUESTIONNAIRE = "Questions";
	public static final String DB_TABLE_QUESTIONNAIRE_DATAID = "DataID";
	public static final String DB_TABLE_QUESTIONNAIRE_ORDERID = "OrderID";
	public static final String DB_TABLE_QUESTIONNAIRE_QTEXT = "Qtext";
	public static final String DB_TABLE_QUESTIONNAIRE_QVALUE = "Qvalue";
	public static final String DB_TABLE_QUESTIONNAIRE_QTL = "QuestionTypeLink";
	public static final String DB_TABLE_QUESTIONNAIRE_OT = "ObjectType";
	public static final String DB_TABLE_QUESTIONNAIRE_FT = "FreeText";

	public static final String DB_TABLE_ANSWERS = "Answers";
	public static final String DB_TABLE_ANSWERS_ANSWERID = "AnswerID";
	public static final String DB_TABLE_ANSWERS_ATEXT = "Atext";
	public static final String DB_TABLE_ANSWERS_AVALUE = "Avalue";
	public static final String DB_TABLE_ANSWERS_DATAID = "DataID";
	public static final String DB_TABLE_ANSWERS_ORDERID = "OrderID";
	public static final String DB_TABLE_ANSWERS_MI = "Mi";
	public static final String DB_TABLE_ANSWERS_BRANCHID = "BranchID";
	public static final String DB_TABLE_ANSWERS_WORKERID = "WorkerID";

	public static final String DB_TABLE_POS = "POSParams";
	public static final String DB_TABLE_POS_ProductId = "ProductId";
	public static final String DB_TABLE_POS_LocationId = "LocationId";
	public static final String DB_TABLE_POS_PropertyId = "PropertyId";
	public static final String DB_TABLE_POS_Price = "Price";
	public static final String DB_TABLE_POS_Quantity = "Quantity";
	public static final String DB_TABLE_POS_SetId = "SetId";
	public static final String DB_TABLE_POS_OrderId = "OrderId";
	public static final String DB_TABLE_POS_Notee = "Notee";
	public static final String DB_TABLE_POS_date = "Expiration";

	public static final String DB_TABLE_ORDERS = "Orders";
	public static final String DB_TABLE_ORDERS_ORDERID = "OrderID";
	public static final String DB_TABLE_ORDERS_STATUS = "Status";

	public static final String DB_TABLE_JobList_rpID = "rpID";
	public static final String DB_TABLE_JobList_ReportTitle = "ReportTitle";
	public static final String DB_TABLE_JobList_ReportOrder = "ReportOrder";
	public static final String DB_TABLE_JobList_URL = "URL";
	public static final String DB_TABLE_JobList_ReportCategory = "ReportCategory";
	public static final String DB_TABLE_JobList_CategoryRow = "CategoryRow";
	public static final String DB_TABLE_JobList_CategoryCol = "CategoryCol";

	public static final String DB_TABLE_JOBLIST = "JobList";
	public static final String DB_TABLE_JOBLIST_ORDERID = "OrderID";
	public static final String DB_TABLE_JOBLIST_DATE = "Date";
	public static final String DB_TABLE_JOBLIST_SN = "StatusName";
	public static final String DB_TABLE_JOBLIST_DESC = "Description";
	public static final String DB_TABLE_JOBLIST_SETNAME = "SetName";
	public static final String DB_TABLE_JOBLIST_SETLINK = "SetLink";
	public static final String DB_TABLE_JOBLIST_CN = "ClientName";
	public static final String DB_TABLE_JOBLIST_BN = "BranchName";
	public static final String DB_TABLE_JOBLIST_BFN = "BranchFullName";
	public static final String DB_TABLE_JOBLIST_CITYNAME = "CityName";
	public static final String DB_TABLE_JOBLIST_ADDRESS = "Address";
	public static final String DB_TABLE_JOBLIST_BP = "BranchPhone";
	public static final String DB_TABLE_JOBLIST_OH = "OpeningHour";
	public static final String DB_TABLE_JOBLIST_TS = "TimeStart";
	public static final String DB_TABLE_JOBLIST_TE = "TimeEnd";
	public static final String DB_TABLE_JOBLIST_SETID = "SetID";
	public static final String DB_TABLE_JOBLIST_BL = "BranchLat";
	public static final String DB_TABLE_JOBLIST_BLNG = "BranchLang";
	public static final String DB_TABLE_JOBLIST_FN = "FullName";
	public static final String DB_TABLE_JOBLIST_JC = "JobCount";
	public static final String DB_TABLE_JOBLIST_JI = "JobIndex";
	public static final String DB_TABLE_JOBLIST_BLINK = "BranchLink";
	public static final String DB_TABLE_JOBLIST_MID = "MassID";
	public static final String DB_TABLE_CHECKER_CODE = "CheckerCode";
	public static final String DB_TABLE_CHECKER_LINK = "CheckerLink";
	public static final String DB_TABLE_BRANCH_CODE = "BranchCode";
	public static final String DB_TABLE_SETCODE = "SetCode";
	public static final String DB_TABLE_PURCHASE_DESCRIPTION = "PurchaseDescription";
	public static final String DB_TABLE_PURCHASE = "Purchase";

	public static final String DB_TABLE_SETS = "Sets";
	public static final String DB_TABLE_SETS_SETID = "SetID";
	public static final String DB_TABLE_SET_NAME = "SetName";
	public static final String DB_TABLE_SET_COMP_LINK = "CompanyLink";
	public static final String DB_TABLE_SET_DESC = "SetDesc";
	public static final String DB_TABLE_SET_CODE = "SetCode";
	public static final String DB_TABLE_SET_SHOWSAVEANDEXIT = "Showsaveandexit";
	public static final String DB_TABLE_SET_SHOWTOC = "ShowToc";
	public static final String DB_TABLE_SET_SHOWPREVIEW = "Showpreview";
	public static final String DB_TABLE_SET_CLIENTNAME = "ClientName";
	public static final String DB_TABLE_SET_SHOWFREETEXT = "Showfreetext";
	public static final String DB_TABLE_SET_ENABLE_NON_ANSWERED_CONFIRMATION = "EnableNonansweredConfirmation";
	public static final String DB_TABLE_SET_ENABLE_QUESTION_NUMBERING_INFORM = "EnableQuestionNumberingInForm";
	public static final String DB_TABLE_SET_ENABLE_VALIDATION_QUESTION = "EnableValidationQuestion";
	public static final String DB_TABLE_SET_ALLOW_CHECKER_TO_SET_FINISHTIME = "AllowCheckerToSetFinishTime";
	public static final String DB_TABLE_SET_ALLOW_CRIT_FILE_UPLOAD = "AllowCritFileUpload";
	public static final String DB_TABLE_SET_ALTLANG_ID = "AltLangID";
	public static final String DB_TABLE_SET_ANSWERS_ACT_AS_SUBMIT = "AnswersActAsSubmit";

	public static final String DB_TABLE_QUES = "Questionnaire";
	public static final String DB_TABLE_QUES_FONT = "Font";
	public static final String DB_TABLE_QUES_COLOR = "Color";
	public static final String DB_TABLE_QUES_SIZE = "Size";
	public static final String DB_TABLE_QUES_BOLD = "Bold";
	public static final String DB_TABLE_QUES_ITALIC = "Italic";
	public static final String DB_TABLE_QUES_UL = "Underline";
	public static final String DB_TABLE_QUES_ATTACHMENT = "Attachment";
	public static final String DB_TABLE_QUES_DATAID = "DataID";
	public static final String DB_TABLE_QUES_OT = "ObjectType";
	public static final String DB_TABLE_QUES_PFN = "PictureFilename";
	public static final String DB_TABLE_QUES_MAND = "Mandatory";
	public static final String DB_TABLE_QUES_DT = "DisplayType";
	public static final String DB_TABLE_QUES_AO = "AnswerOedering";
	public static final String DB_TABLE_QUES_MT = "MiType";
	public static final String DB_TABLE_QUES_MM = "MiMandatory";
	public static final String DB_TABLE_QUES_MD = "MiDesc";
	public static final String DB_TABLE_QUES_Q = "Question";
	public static final String DB_TABLE_QUES_QD = "Qdesc";
	public static final String DB_TABLE_QUES_QTL = "QTypeLink";
	public static final String DB_TABLE_QUES_UIT = "UseInToc";
	public static final String DB_TABLE_QUES_SID = "SetID";
	public static final String DB_TABLE_QUES_TEXT = "Text";
	public static final String DB_TABLE_QUES_TID = "TextID";
	public static final String DB_TABLE_QUES_OBJECTDISCONDITINO = "ObjectDisplayCondition";
	public static final String DB_TABLE_QUES_OBJECTCODE = "ObjectCode";
	public static final String DB_TABLE_QUES_MI_MIN = "MiNumberMin";
	public static final String DB_TABLE_QUES_MI_MAX = "MiNumberMax";
	public static final String DB_TABLE_QUES_MAX_ANSWER = "MaxAnswersForMultiple";
	public static final String DB_TABLE_QUES_MI_FREE_TEXT_MIN = "MiFreeTextMinlength";
	public static final String DB_TABLE_QUES_MI_FREE_TEXT_MAX = "MiFreeTextMaxlength";
	public static final String DB_TABLE_QUES_URLCONTENT = "UrlContent";
	public static final String DB_TABLE_QUES_DESTINATION_OBJECT = "DestinationObject";
	public static final String DB_TABLE_QUES_URL_ID = "UrlID";
	public static final String DB_TABLE_QUES_DEST_DESC = "DestinationDescription";
	public static final String DB_TABLE_QUES_WORKERINPUTCAPTION = "WorkerInputCaption";
	public static final String DB_TABLE_QUES_BRANCHINPUTCAPTION = "BranchInputCaption";

	public static final String DB_TABLE_WORKERS = "Workers";
	public static final String DB_TABLE_WORKERS_WID = "workersID";
	public static final String DB_TABLE_WORKERS_SETID = "SetID";
	public static final String DB_TABLE_WORKERS_WORKERID = "workerID";
	public static final String DB_TABLE_WORKERS_WN = "workerName";
	public static final String DB_TABLE_WORKERS_BL = "BranchLink";

	public static final String DB_TABLE_BRANCHES = "Branches";
	public static final String DB_TABLE_BRANCHES_BID = "BranchID";
	public static final String DB_TABLE_BRANCHES_SETID = "SetID";
	public static final String DB_TABLE_BRANCHES_BN = "BranchName";

	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values = "ProductPropertiesValues";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_Content = "Content";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_PropLink = "PropLink";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_Order = "OrderLink";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_IsActive = "IsActive";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_SetId = "SetId";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_ValueID = "ValueID";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Values_ProdPropID = "ProdPropID";

	public static final String DB_TABLE_PRODUCT_PROPERTIES = "ProductProperties";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_ProdPropID = "ProdPropID";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_PropertyName = "PropertyName";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_ClientLink = "ClientLink";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Mandatory = "Mandatory";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_Order = "OrderLink";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_AllowOtherAddition = "AllowOtherAddition";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_IsActive = "IsActive";
	public static final String DB_TABLE_PRODUCT_PROPERTIES_SetId = "SetId";

	public static final String DB_TABLE_PRODUCT_LOCATION = "ProductLocation";
	public static final String DB_TABLE_PRODUCT_LOCATION_PID = "ProdLocationID";
	public static final String DB_TABLE_PRODUCT_LOCATION_SETID = "SetId";
	public static final String DB_TABLE_PRODUCT_LOCATION_location = "ProductLocation";
	public static final String DB_TABLE_PRODUCT_CLientLink = "ClientLink";

	public static final String DB_TABLE_PRODUCTS = "Products";
	public static final String DB_TABLE_PRODUCTS_PID = "ProductID";
	public static final String DB_TABLE_PRODUCTS_SETID = "SetId";
	public static final String DB_TABLE_PRODUCTS_PN = "ProductName";
	public static final String DB_TABLE_PRODUCTS_ClientLink = "ClientLink";
	public static final String DB_TABLE_PRODUCTS_IsActive = "IsActive";
	public static final String DB_TABLE_PRODUCTS_ProductCode = "ProductCode";
	public static final String DB_TABLE_PRODUCTS_CheckQuantity = "CheckQuantity";
	public static final String DB_TABLE_PRODUCTS_CheckShelfLevel = "CheckShelfLevel";
	public static final String DB_TABLE_PRODUCTS_CheckPrice = "CheckPrice";
	public static final String DB_TABLE_PRODUCTS_CheckPacking = "CheckPacking";
	public static final String DB_TABLE_PRODUCTS_CheckExpiration = "CheckExpiration";
	public static final String DB_TABLE_PRODUCTS_AddNote = "AddNote";
	public static final String DB_TABLE_PRODUCTS_TakePicture = "TakePicture";
	public static final String DB_TABLE_PRODUCTS_Size = "Size";
	public static final String DB_TABLE_PRODUCTS_Order = "OrderLink";
	public static final String DB_TABLE_PRODUCTS_Bold = "Bold";
	public static final String DB_TABLE_PRODUCTS_prop_id_51 = "prop_id_51";
	public static final String DB_TABLE_PRODUCTS_prop_id_52 = "prop_id_52";

	public static final String DB_TABLE_QA = "QuestionnaireAnswers";
	public static final String DB_TABLE_QA_SETID = "SetID";
	public static final String DB_TABLE_QA_DID = "DataID";
	public static final String DB_TABLE_QA_AID = "AnsID";
	public static final String DB_TABLE_QA_ANS = "Answer";
	public static final String DB_TABLE_QA_VAL = "Value";
	public static final String DB_TABLE_QA_COL = "Color";
	public static final String DB_TABLE_QA_BOLD = "Bold";
	public static final String DB_TABLE_QA_ITALIC = "Italic";
	public static final String DB_TABLE_QA_UL = "UnderLine";
	public static final String DB_TABLE_QA_CODE = "Code";
	public static final String DB_TABLE_QA_JUMPTO = "JumpTo";
	public static final String DB_TABLE_QA_ICONNAME = "IconName";
	public static final String DB_TABLE_QA_HIDEADDITIONALINFO = "HideAdditionalInfo";
	public static final String DB_TABLE_QA_ADDITIONALINFOMANDATORY = "AdditionalInfoMandatory";

	// QUETIONNAIRE MENU ITEMS ID
	public static final int MENUID_EXIT_AND_SAVE = 101;
	public static final int MENUID_PREVIEW = 102;
	public static final int MENUID_SUBMIT_SURVEY = 103;
	public static final int MENUID_UPDALOAD_COMPLETE_JOBS = 104;
	public static final int MENUID_DOWNLOAD_UPDATED_JOBS = 105;
	public static final int MENUID_DOWNLOAD_EXIT_JOBLIST = 106;
	public static final int MENUID_DOWNLOAD_SETTINGS = 107;
	public static final int MENUID_EXIT_AND_DELETE = 108;
	public static final int MENUID_DONE = 109;
	public static final int MENUID_ATTACHED_FILE = 110;
	public static final int MENUID_IMAGE_GALLERY_OPTION = 111;
	public static final int MENUID_CAMERA_OPTION = 112;
	public static final int MENUID_VIDEO_OPTION = 113;
	public static final int MENUID_AUDIO_OPTION = 114;
}
