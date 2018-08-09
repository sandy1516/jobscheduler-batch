CREATE DATABASE users_job;

CREATE TABLE [dbo].[users](
	[name] [nvarchar](50) NULL,
	[dob] [nvarchar](50) NULL,
	[contact] [nvarchar](50) NULL
) ON [PRIMARY]


INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Sandeep', '16-05-1991', '9454364605');
INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Rahul', '06-05-1991', '9458366875');
INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Muthu', '16-05-1991', '9454364605');
INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Elavarasan', '16-05-1991', '9454364605');
INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Retna', '16-05-1991', '9454364605');
INSERT INTO users_job.dbo.users (name,dob,contact)
VALUES ('Prabu', '16-05-1991', '9454364605');