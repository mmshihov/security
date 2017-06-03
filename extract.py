import shutil
import sys

srcDir=''
if len(sys.argv)<=1:
    print('usage... copy *.pdf files into ../pdfs folder')
    srcDir = '../pdfs/'
else:
    srcDir = sys.argv[1]

shutil.copyfile('01-information/main.beamer.pdf',               srcDir + '01-information.pdf')
shutil.copyfile('03-threats/main.beamer.pdf',                   srcDir + '02-threats.pdf')
shutil.copyfile('05-laworg/main.beamer.pdf',                    srcDir + '03-protection-methods.pdf')
shutil.copyfile('06-policy/main.beamer.pdf',                    srcDir + '04-policy-standards.pdf')
shutil.copyfile('07.0-authentication_pwd/main.beamer.pdf',      srcDir + '05-know-have-authentication.pdf')
shutil.copyfile('07.1-authentication_bio/main.beamer.pdf',      srcDir + '06-biometrics-are-can-authentication.pdf')
shutil.copyfile('07.2-authentication_hware/main.beamer.pdf',    srcDir + '07-hw-key-have-authentication.pdf')
shutil.copyfile('08-accesscontrol/main.beamer.pdf',             srcDir + '08-access-control.pdf')
#shutil.copyfile('09-hardware/main.beamer.pdf',                  srcDir + '09-.pdf')
shutil.copyfile('10.0-integrity/main.beamer.pdf',               srcDir + '09-common-integrity.pdf')
shutil.copyfile('10.1-integrity_lbc/main.beamer.pdf',           srcDir + '10-lbc-integrity.pdf')
shutil.copyfile('10.1-integrity_raid/main.beamer.pdf',          srcDir + '11-archieve-raid-integrity.pdf')
shutil.copyfile('14-math/main.beamer.pdf',                      srcDir + '12-math-base-cryptography.pdf')
shutil.copyfile('15-cryptography/main.beamer.pdf',              srcDir + '13-intro-ciphers-cryptography.pdf')
shutil.copyfile('15.1-cryptography/main.beamer.pdf',            srcDir + '14-block-and-stream-ciphers.pdf')
shutil.copyfile('16-secretsplitting/main.beamer.pdf',           srcDir + '15-secret-splitting.pdf')
shutil.copyfile('17-intrusion/main.beamer.pdf',                 srcDir + '16-intrusion-attack.pdf')
shutil.copyfile('18-network/main.beamer.pdf',                   srcDir + '17-nat-vpn-firewall-networks.pdf')

shutil.copyfile('exam/main.article.pdf',                        srcDir + 'x.pdf')
