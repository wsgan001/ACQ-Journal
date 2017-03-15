set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [6413.437293	6329.695391	6319.079554	6319.079554	6336.290676];
y2 = [1929.751462	1913.462108	1897.369628	1890.154476	1886.454498];
y3 = [109.485488	56.9613229	38.07068085	24.53292229	19.68013537];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the value of \theta');
ylabel('time (ms)');

axis([0.5 5.5 0.0 11000]);

%set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
set(gca, 'xtick', 1:5, 'XTickLabel', {'0.2','0.4','0.6','0.8','1.0'});
leg=legend('basic-g-v1','basic-w-v1','SWT', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);