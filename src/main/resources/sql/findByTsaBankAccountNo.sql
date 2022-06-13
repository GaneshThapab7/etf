DROP FUNCTION IF EXISTS gettsabybankaccount( accountNo varchar);
CREATE FUNCTION gettsabybankaccount( accountNo varchar)
    RETURNS TABLE
    (

    id                                  integer,
    tsa_bank_code                           varchar,
    tsa_bank_account_no	                    varchar,
    nchl_debtor_bank_code                         timestamp,
    nchl_debtor_branch_code  	                timestamp

    )
    LANGUAGE oracale
AS
    $function$
DECLARE
    QUERY int ;
BEGIN
    RETURN QUERY
    select
        c.*
    from "tsa_debtors_bank_detail" c where c."tsa_bank_account_no"=accountNo;
END;
$function$;