create table share_account_info (
    sai_seq	int	not null	auto_increment	primary key,
sai_name	varchar(255)	null		,
sai_start_day	datetime	null		,
sai_code	varchar(50)	not null		
)

create table member_basic_info		(
    mbi_seq	int	not null	auto_increment	primary key,
mbi_password	varchar(255)	not null		,
mbi_gender	int	not null		,
mbi_name	int	not null		,
mbi_start_day	datetime	not null		,
mbi_birth	datetime	not null		,
mbi_nickname	varchar(255)	not null		,
mbi_sai_seq	int	not null		
)

create table category_info		(
    cate_seq	int	not null	auto_increment	primary key,
cate_name	varchar(255)	not null		
)

create table background_img_info		(
    bii_seq	int	not null	auto_increment	primary key,
bii_filename	varchar(50)	not null		,
bii_uri	varchar(50)	not null		
)

create table notioce_img_info		(
    nii_seq	int	not null	auto_increment	primary key,
nii_filename	varchar(50)	not null	,	
nii_uri	varchar(50)	not null		
)

create table schedule_img_info		(
    sii_seq	int	not null	auto_increment	primary key,
sii_filename	varchar(50)	not null		,
sii_uri	varchar(50)	not null		
)

create table schedule_info		(
    si_seq	int	not null	auto_increment	primary key,
si_start_date	Localdate	not null		,
si_end_date	Localdate	not null		,
si_memo	varchar(255)	not null		,
si_sii_seq	varchar(255)	not null		,
si_mi_seq	int	not null		
)

create table notice_info		(
    ni_seq	int	not null	auto_increment	primary key,
ni_memo	varchar(255)	not null		,
ni_date	Localdate	not null		,
ni_sii_seq	int	not null		
)

create table account_book_img		(
    ai_seq	int	not null	auto_increment	primary key,
ai_img_name	varchar(50)	null		,
ai_uri	varchar(50)	null		
)

create table import_Info		(
    ii_seq	int	not null	auto_increment	primary key,
ii_price	int	null		,
ii_memo	varchar(255)	null		,
ii_date	datetime	not null		,
ii_mbi_seq	int	null		,
ii_cate_seq	int	null		
)

create table expense_Info		(
    ei_seq	int	not null	auto_increment	primary key,
ei_price	int	null		,
ei_memo	varchar(255)	null		,
ei_date	datetime	not null	,	
ei_status	int	null	default 1	,
ii_mbi_seq	int	null		,
ei_ai_seq	int	null		,
ei_cate_seq	int	null		
)
